package org.example.intellibookmallapi.recommend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.mapper.UserMapper;
import org.example.intellibookmallapi.recommend.model.ItemCooccurrence;
import org.example.intellibookmallapi.recommend.model.UserBehaviorVector;
import org.example.intellibookmallapi.recommend.model.UserSimilarity;
import org.example.intellibookmallapi.recommend.service.RecommendService;
import org.example.intellibookmallapi.recommend.service.UserBehaviorService;
import org.example.intellibookmallapi.recommend.util.SimilarityCalculator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐服务实现类
 * 实现基于协同过滤的个性化图书推荐
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    
    private final UserBehaviorService userBehaviorService;
    private final SimilarityCalculator similarityCalculator;
    private final EBookMapper ebookMapper;
    private final UserMapper userMapper;
    
    /**
     * 获取用户个性化推荐
     * 主推荐算法：协同过滤 + 冷启动
     */
    @Override
    public List<EBook> getRecommendations(Long userId, Integer limit) {
        // 前置条件检查
        if (userId == null) {
            log.warn("用户ID为空，返回冷启动推荐");
            return getColdStartRecommendations(limit);
        }
        
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }
        
        log.info("开始为用户 {} 生成推荐，数量限制: {}", userId, limit);
        
        // 步骤 1: 构建用户行为向量
        UserBehaviorVector userVector = userBehaviorService.buildUserBehaviorVector(userId);
        
        // 步骤 2: 判断是否为新用户（冷启动）
        if (!userVector.hasAnyBehavior()) {
            log.info("用户 {} 无历史行为数据，使用冷启动推荐", userId);
            return getColdStartRecommendations(limit);
        }
        
        // 步骤 3: 协同过滤推荐
        List<EBook> recommendations = getCollaborativeFilteringRecommendations(userId, limit);
        
        // 步骤 4: 如果协同过滤推荐结果不足，补充冷启动推荐
        if (recommendations.size() < limit) {
            int remaining = limit - recommendations.size();
            List<EBook> coldStartBooks = getColdStartRecommendations(remaining);
            
            // 合并结果，去重
            Set<Long> existingBookIds = recommendations.stream()
                    .map(EBook::getBookId)
                    .collect(Collectors.toSet());
            
            for (EBook book : coldStartBooks) {
                if (!existingBookIds.contains(book.getBookId())) {
                    recommendations.add(book);
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        // 步骤 5: 截取前 limit 条
        List<EBook> finalResults = recommendations.stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        log.info("为用户 {} 生成了 {} 条推荐", userId, finalResults.size());
        
        // 后置条件检查
        assert finalResults.size() <= limit : "推荐结果数量不能超过 limit";
        assert finalResults.stream().allMatch(book -> book.getStatus() != null && book.getStatus() == 1 
                && (book.getIsDeleted() == null || book.getIsDeleted() == 0)) 
                : "所有推荐图书必须为上架且未删除状态";
        
        return finalResults;
    }
    
    /**
     * 获取基于协同过滤的推荐（User-CF + Item-CF）
     */
    @Override
    public List<EBook> getCollaborativeFilteringRecommendations(Long userId, Integer limit) {
        if (userId == null || limit == null || limit <= 0) {
            return new ArrayList<>();
        }
        
        log.debug("开始为用户 {} 生成协同过滤推荐", userId);
        
        // 构建用户行为向量
        UserBehaviorVector userVector = userBehaviorService.buildUserBehaviorVector(userId);
        
        // User-CF 推荐
        List<EBook> userCFResults = getUserBasedCFRecommendations(userId, userVector, limit);
        log.debug("User-CF 推荐结果数量: {}", userCFResults.size());
        
        // Item-CF 推荐
        List<EBook> itemCFResults = getItemBasedCFRecommendations(userId, userVector, limit);
        log.debug("Item-CF 推荐结果数量: {}", itemCFResults.size());
        
        // 合并去重（User-CF 优先）
        List<EBook> mergedResults = mergeAndDeduplicate(userCFResults, itemCFResults);
        
        // 过滤已购买图书
        List<EBook> filteredResults = filterPurchasedBooks(mergedResults, userId);
        
        // 只保留上架且未删除的图书
        List<EBook> finalResults = filterOnSaleBooks(filteredResults);
        
        log.debug("协同过滤推荐最终结果数量: {}", finalResults.size());
        
        return finalResults;
    }
    
    /**
     * 基于用户的协同过滤推荐（User-CF）
     */
    private List<EBook> getUserBasedCFRecommendations(Long userId, UserBehaviorVector userVector, Integer limit) {
        log.debug("开始 User-CF 推荐");
        
        // 步骤 1: 获取所有用户ID（简化实现，实际应该分批处理）
        // 这里我们只获取有行为数据的用户
        List<Long> allUserIds = getAllActiveUserIds();
        
        if (allUserIds.isEmpty()) {
            log.warn("没有找到其他活跃用户");
            return new ArrayList<>();
        }
        
        // 步骤 2: 构建所有用户的行为向量
        List<UserBehaviorVector> allUserVectors = new ArrayList<>();
        for (Long otherUserId : allUserIds) {
            if (!otherUserId.equals(userId)) {
                UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(otherUserId);
                if (vector.hasAnyBehavior()) {
                    allUserVectors.add(vector);
                }
            }
        }
        
        if (allUserVectors.isEmpty()) {
            log.warn("没有找到其他有行为数据的用户");
            return new ArrayList<>();
        }
        
        // 步骤 3: 计算相似度，获取前50个最相似用户
        List<UserSimilarity> similarUsers = similarityCalculator.calculateUserSimilarity(
                userId, userVector, allUserVectors, 50);
        
        if (similarUsers.isEmpty()) {
            log.warn("没有找到相似用户");
            return new ArrayList<>();
        }
        
        log.debug("找到 {} 个相似用户", similarUsers.size());
        
        // 步骤 4: 收集相似用户购买过的图书，计算加权分数
        Map<Long, Double> bookScores = new HashMap<>();
        
        for (UserSimilarity simUser : similarUsers) {
            UserBehaviorVector simUserVector = userBehaviorService.buildUserBehaviorVector(simUser.getUserId());
            Set<Long> purchasedBooks = simUserVector.getPurchasedBooks();
            
            for (Long bookId : purchasedBooks) {
                // 累加加权分数：相似度 × 用户权重
                bookScores.merge(bookId, simUser.getSimilarity(), Double::sum);
            }
        }
        
        // 步骤 5: 按分数降序排序
        List<Long> rankedBookIds = bookScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        
        // 步骤 6: 查询图书详情
        List<EBook> recommendations = getBooksByIds(rankedBookIds);
        
        log.debug("User-CF 推荐了 {} 本图书", recommendations.size());
        
        return recommendations;
    }
    
    /**
     * 基于物品的协同过滤推荐（Item-CF）
     */
    private List<EBook> getItemBasedCFRecommendations(Long userId, UserBehaviorVector userVector, Integer limit) {
        log.debug("开始 Item-CF 推荐");
        
        // 步骤 1: 获取用户已购买和收藏的图书
        Set<Long> userBooks = new HashSet<>();
        userBooks.addAll(userVector.getPurchasedBooks());
        userBooks.addAll(userVector.getFavoriteBooks());
        
        if (userBooks.isEmpty()) {
            log.debug("用户没有购买或收藏的图书，无法进行 Item-CF 推荐");
            return new ArrayList<>();
        }
        
        // 步骤 2: 构建物品共现矩阵
        Map<Long, List<ItemCooccurrence>> cooccurrenceMatrix = buildItemCooccurrenceMatrix();
        
        // 步骤 3: 收集相似图书
        Map<Long, Double> bookScores = new HashMap<>();
        
        for (Long bookId : userBooks) {
            List<ItemCooccurrence> similarBooks = cooccurrenceMatrix.get(bookId);
            
            if (similarBooks != null) {
                for (ItemCooccurrence cooccur : similarBooks) {
                    Long similarBookId = cooccur.getBookId2();
                    Double score = cooccur.getSimilarity();
                    
                    bookScores.merge(similarBookId, score, Double::sum);
                }
            }
        }
        
        // 步骤 4: 按分数降序排序
        List<Long> rankedBookIds = bookScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        
        // 步骤 5: 查询图书详情
        List<EBook> recommendations = getBooksByIds(rankedBookIds);
        
        log.debug("Item-CF 推荐了 {} 本图书", recommendations.size());
        
        return recommendations;
    }
    
    /**
     * 构建物品共现矩阵
     */
    private Map<Long, List<ItemCooccurrence>> buildItemCooccurrenceMatrix() {
        log.debug("开始构建物品共现矩阵");
        
        Map<Long, List<ItemCooccurrence>> matrix = new HashMap<>();
        
        // 获取所有用户ID
        List<Long> allUserIds = getAllActiveUserIds();
        
        // 统计图书对的共现次数
        Map<String, Integer> cooccurrenceCount = new HashMap<>();
        
        for (Long userId : allUserIds) {
            UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(userId);
            
            // 合并购买和收藏的图书
            Set<Long> userBooks = new HashSet<>();
            userBooks.addAll(vector.getPurchasedBooks());
            userBooks.addAll(vector.getFavoriteBooks());
            
            // 计算图书对的共现
            List<Long> bookList = new ArrayList<>(userBooks);
            for (int i = 0; i < bookList.size(); i++) {
                for (int j = i + 1; j < bookList.size(); j++) {
                    Long bookId1 = bookList.get(i);
                    Long bookId2 = bookList.get(j);
                    
                    // 确保 bookId1 < bookId2，避免重复
                    if (bookId1 > bookId2) {
                        Long temp = bookId1;
                        bookId1 = bookId2;
                        bookId2 = temp;
                    }
                    
                    String key = bookId1 + "_" + bookId2;
                    cooccurrenceCount.merge(key, 1, Integer::sum);
                }
            }
        }
        
        // 构建共现矩阵（只保留共现次数 >= 2 的图书对）
        for (Map.Entry<String, Integer> entry : cooccurrenceCount.entrySet()) {
            if (entry.getValue() >= 2) {
                String[] parts = entry.getKey().split("_");
                Long bookId1 = Long.parseLong(parts[0]);
                Long bookId2 = Long.parseLong(parts[1]);
                Integer count = entry.getValue();
                
                // 计算相似度（简单使用共现次数作为相似度）
                Double similarity = count.doubleValue();
                
                // 添加到矩阵（双向）
                ItemCooccurrence cooccur1 = new ItemCooccurrence(bookId1, bookId2, count, similarity);
                matrix.computeIfAbsent(bookId1, k -> new ArrayList<>()).add(cooccur1);
                
                ItemCooccurrence cooccur2 = new ItemCooccurrence(bookId2, bookId1, count, similarity);
                matrix.computeIfAbsent(bookId2, k -> new ArrayList<>()).add(cooccur2);
            }
        }
        
        // 对每个图书的相似图书列表按相似度降序排序
        for (List<ItemCooccurrence> list : matrix.values()) {
            list.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));
        }
        
        log.debug("物品共现矩阵构建完成，包含 {} 个图书", matrix.size());
        
        return matrix;
    }
    
    /**
     * 获取冷启动推荐（热度推荐）
     */
    @Override
    public List<EBook> getColdStartRecommendations(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }
        
        log.debug("开始生成冷启动推荐，数量: {}", limit);
        
        // 查询所有上架图书
        List<EBook> allBooks = ebookMapper.selectPopularEBooks(limit * 2); // 多查询一些以便计算热度
        
        if (allBooks.isEmpty()) {
            log.warn("没有找到上架图书");
            return new ArrayList<>();
        }
        
        // 计算综合热度分数并排序
        List<EBook> rankedBooks = allBooks.stream()
                .map(book -> {
                    // 热度公式：rating * 0.4 + log(downloadCount + 1) * 0.3 + log(viewCount + 1) * 0.3
                    double ratingScore = (book.getRating() != null ? book.getRating() : 0.0) * 0.4;
                    int downloadCount = (book.getDownloadCount() != null ? book.getDownloadCount() : 0);
                    int viewCount = (book.getViewCount() != null ? book.getViewCount() : 0);
                    double downloadScore = Math.log(downloadCount + 1) * 0.3;
                    double viewScore = Math.log(viewCount + 1) * 0.3;
                    
                    double totalScore = ratingScore + downloadScore + viewScore;
                    book.setTempScore(totalScore); // 临时存储分数
                    
                    return book;
                })
                .sorted((a, b) -> Double.compare(b.getTempScore(), a.getTempScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        log.debug("冷启动推荐生成了 {} 本图书", rankedBooks.size());
        
        return rankedBooks;
    }
    
    /**
     * 合并去重推荐结果（User-CF 优先）
     */
    private List<EBook> mergeAndDeduplicate(List<EBook> userCFResults, List<EBook> itemCFResults) {
        LinkedHashSet<Long> bookIds = new LinkedHashSet<>();
        List<EBook> merged = new ArrayList<>();
        
        // 先添加 User-CF 结果
        for (EBook book : userCFResults) {
            if (bookIds.add(book.getBookId())) {
                merged.add(book);
            }
        }
        
        // 再添加 Item-CF 结果
        for (EBook book : itemCFResults) {
            if (bookIds.add(book.getBookId())) {
                merged.add(book);
            }
        }
        
        return merged;
    }
    
    /**
     * 过滤已购买图书
     */
    private List<EBook> filterPurchasedBooks(List<EBook> books, Long userId) {
        UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(userId);
        Set<Long> purchasedBookIds = vector.getPurchasedBooks();
        
        return books.stream()
                .filter(book -> !purchasedBookIds.contains(book.getBookId()))
                .collect(Collectors.toList());
    }
    
    /**
     * 只保留上架且未删除的图书
     */
    private List<EBook> filterOnSaleBooks(List<EBook> books) {
        return books.stream()
                .filter(book -> book.getStatus() != null && book.getStatus() == 1 
                        && (book.getIsDeleted() == null || book.getIsDeleted() == 0))
                .collect(Collectors.toList());
    }
    
    /**
     * 根据图书ID列表查询图书详情
     */
    private List<EBook> getBooksByIds(List<Long> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<EBook> books = new ArrayList<>();
        for (Long bookId : bookIds) {
            EBook book = ebookMapper.selectByPrimaryKey(bookId);
            if (book != null && book.getStatus() != null && book.getStatus() == 1 
                    && (book.getIsDeleted() == null || book.getIsDeleted() == 0)) {
                books.add(book);
            }
        }
        
        return books;
    }
    
    /**
     * 获取所有活跃用户ID
     * 简化实现：返回所有用户ID
     */
    private List<Long> getAllActiveUserIds() {
        // 这里简化实现，实际应该从数据库查询
        // 由于没有查询所有用户的方法，我们使用一个简化的实现
        // 在实际应用中，应该添加相应的 Mapper 方法
        List<Long> userIds = new ArrayList<>();
        
        // 从用户行为数据中提取用户ID
        // 这是一个简化的实现，实际应该有专门的查询方法
        for (long i = 1; i <= 100; i++) { // 假设用户ID范围
            if (userMapper.selectByPrimaryKey(i) != null) {
                userIds.add(i);
            }
        }
        
        return userIds;
    }
}
