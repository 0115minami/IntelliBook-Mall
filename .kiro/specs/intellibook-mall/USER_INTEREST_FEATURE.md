# 用户兴趣管理功能设计文档

## 功能概述

用户兴趣管理是 IntelliBook-Mall 推荐系统的重要组成部分，允许用户主动选择和管理自己感兴趣的电子书分类，从而获得更精准的个性化推荐。

## 核心价值

### 1. 解决冷启动问题
- **新用户困境**: 传统推荐系统依赖用户历史行为，新用户无历史数据导致推荐效果差
- **解决方案**: 用户注册时主动选择兴趣标签，系统立即提供个性化推荐
- **效果**: 新用户首次访问即可获得符合偏好的书籍推荐

### 2. 提升推荐准确度
- **用户主导**: 用户明确表达兴趣偏好，比被动分析行为更准确
- **混合策略**: 结合兴趣标签和行为数据的混合推荐算法
- **动态调整**: 用户可随时调整兴趣标签，推荐结果实时更新

### 3. 增强用户体验
- **个性化控制**: 用户掌握推荐内容的主动权
- **透明度**: 用户清楚知道推荐来源（基于兴趣标签）
- **满意度**: 推荐内容更符合用户期望，提升满意度

## 数据库设计

### 表结构

```sql
CREATE TABLE IF NOT EXISTS tb_user_interest (
    interest_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id),
    UNIQUE(user_id, category_id)  -- 防止重复添加
);

CREATE INDEX idx_user_interest_user ON tb_user_interest(user_id);
CREATE INDEX idx_user_interest_category ON tb_user_interest(category_id);
```

### 字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| interest_id | INTEGER | 主键，自增ID |
| user_id | INTEGER | 用户ID，外键关联 tb_user |
| category_id | INTEGER | 分类ID，外键关联 tb_category |
| create_time | DATETIME | 创建时间 |

### 约束说明

- **唯一约束**: (user_id, category_id) 防止用户重复添加同一分类
- **外键约束**: 确保数据完整性
- **索引优化**: 提升查询性能

## 功能设计

### 1. 兴趣选择流程

#### 新用户注册流程
```
用户注册 → 注册成功 → 兴趣选择引导页 → 选择3-10个分类 → 完成设置 → 进入首页
```

#### 引导页设计要点
- 展示所有一级分类（计算机、文学、经济等）
- 支持多选，建议选择3-10个
- 提供"跳过"选项（跳过则展示热门推荐）
- 可展开查看二级分类详情

#### 示例分类列表
```
✓ 计算机与互联网
  - Java
  - Python
  - 前端开发
  - 算法与数据结构

✓ 文学小说
  - 科幻小说
  - 悬疑推理
  - 文学经典

□ 经济管理
  - 企业管理
  - 市场营销
  - 金融投资
```

### 2. 兴趣管理功能

#### 个人中心 - 兴趣管理
- 查看当前已选择的兴趣标签
- 添加新的兴趣标签
- 删除不感兴趣的标签
- 查看每个兴趣标签下的推荐书籍数量

#### 操作界面示例
```
我的兴趣标签

[计算机与互联网] [×]  推荐书籍: 156本
[文学小说] [×]         推荐书籍: 89本
[经济管理] [×]         推荐书籍: 45本

[+ 添加兴趣标签]
```

### 3. 推荐算法集成

#### 基于兴趣的推荐算法

```java
public List<EBook> getInterestBasedRecommendations(Long userId, Integer limit) {
    // 1. 获取用户兴趣分类
    List<Long> interestCategoryIds = userInterestMapper.getUserInterestCategories(userId);
    
    // 2. 如果用户未设置兴趣，返回热门书籍
    if (interestCategoryIds.isEmpty()) {
        return getHotBooks(limit);
    }
    
    // 3. 从用户感兴趣的分类中获取高评分书籍
    List<EBook> recommendations = new ArrayList<>();
    int booksPerCategory = limit / interestCategoryIds.size() + 1;
    
    for (Long categoryId : interestCategoryIds) {
        List<EBook> categoryBooks = eBookMapper.getTopRatedBooksByCategory(
            categoryId, booksPerCategory);
        recommendations.addAll(categoryBooks);
    }
    
    // 4. 按评分和浏览量排序，返回指定数量
    return recommendations.stream()
        .sorted(Comparator.comparing(EBook::getRating)
            .thenComparing(EBook::getViewCount).reversed())
        .limit(limit)
        .collect(Collectors.toList());
}
```

#### 混合推荐策略

```java
public List<EBook> getHybridRecommendations(Long userId, Integer limit) {
    List<EBook> result = new ArrayList<>();
    
    // 40% 基于兴趣的推荐
    List<EBook> interestBooks = getInterestBasedRecommendations(userId, limit * 4 / 10);
    result.addAll(interestBooks);
    
    // 30% 基于行为的协同过滤推荐
    List<EBook> behaviorBooks = getCollaborativeFilteringBooks(userId, limit * 3 / 10);
    result.addAll(behaviorBooks);
    
    // 30% 热门书籍推荐
    List<EBook> hotBooks = getHotBooks(limit * 3 / 10);
    result.addAll(hotBooks);
    
    // 去重并返回
    return result.stream()
        .distinct()
        .limit(limit)
        .collect(Collectors.toList());
}
```

## API 接口设计

### 1. 获取用户兴趣列表

**请求**:
```http
GET /api/v1/user/interests
Authorization: Bearer {token}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "success",
  "data": [
    {
      "categoryId": 1,
      "categoryName": "计算机与互联网",
      "categoryNameEn": "Computer & Internet",
      "bookCount": 156
    },
    {
      "categoryId": 2,
      "categoryName": "文学小说",
      "categoryNameEn": "Literature & Fiction",
      "bookCount": 89
    }
  ]
}
```

### 2. 添加兴趣（单个）

**请求**:
```http
POST /api/v1/user/interests
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 3
}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "添加成功"
}
```

### 3. 批量添加兴趣

**请求**:
```http
POST /api/v1/user/interests/batch
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryIds": [1, 2, 3, 4]
}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "批量添加成功",
  "data": {
    "successCount": 4,
    "failedCount": 0
  }
}
```

### 4. 删除兴趣

**请求**:
```http
DELETE /api/v1/user/interests/3
Authorization: Bearer {token}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "删除成功"
}
```

### 5. 检查兴趣状态

**请求**:
```http
GET /api/v1/user/interests/check/1
Authorization: Bearer {token}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "success",
  "data": {
    "isInterested": true
  }
}
```

### 6. 基于兴趣的推荐

**请求**:
```http
GET /api/v1/user/interests/recommendations?limit=10
Authorization: Bearer {token}
```

**响应**:
```json
{
  "resultCode": 200,
  "message": "success",
  "data": [
    {
      "bookId": 1,
      "bookTitle": "Java 编程思想",
      "author": "Bruce Eckel",
      "coverImg": "covers/1.jpg",
      "price": 8800,
      "rating": 4.8,
      "categoryName": "计算机与互联网"
    }
  ]
}
```

## 实现要点

### 1. 数据验证

```java
public Boolean addInterest(Long userId, Long categoryId) {
    // 验证分类是否存在
    EBookCategory category = categoryMapper.selectByPrimaryKey(categoryId);
    if (category == null) {
        throw new IntelliBookException("分类不存在");
    }
    
    // 检查是否已添加
    if (checkIfInterested(userId, categoryId)) {
        throw new IntelliBookException("已添加该兴趣");
    }
    
    // 限制兴趣数量（建议不超过10个）
    int currentCount = userInterestMapper.countByUserId(userId);
    if (currentCount >= 10) {
        throw new IntelliBookException("兴趣标签数量已达上限");
    }
    
    // 添加兴趣
    UserInterest interest = new UserInterest();
    interest.setUserId(userId);
    interest.setCategoryId(categoryId);
    return userInterestMapper.insert(interest) > 0;
}
```

### 2. 性能优化

- **缓存用户兴趣**: 使用 Redis 缓存用户兴趣列表，减少数据库查询
- **批量查询**: 一次性获取所有兴趣分类的书籍，避免 N+1 查询
- **异步更新**: 推荐结果异步计算，提升响应速度

### 3. 用户体验优化

- **智能推荐**: 新用户未设置兴趣时，展示热门书籍和编辑推荐
- **引导提示**: 在首页提示用户设置兴趣标签以获得更好的推荐
- **动态更新**: 用户修改兴趣后，推荐结果实时更新

## 测试用例

### 1. 功能测试

```java
@Test
void testAddInterest() {
    // 测试添加兴趣
    Boolean result = userInterestService.addInterest(1L, 1L);
    assertTrue(result);
    
    // 测试重复添加
    assertThrows(IntelliBookException.class, () -> {
        userInterestService.addInterest(1L, 1L);
    });
}

@Test
void testBatchAddInterests() {
    List<Long> categoryIds = Arrays.asList(1L, 2L, 3L);
    Boolean result = userInterestService.batchAddInterests(1L, categoryIds);
    assertTrue(result);
    
    List<EBookCategory> interests = userInterestService.getUserInterests(1L);
    assertEquals(3, interests.size());
}

@Test
void testInterestBasedRecommendations() {
    // 添加兴趣
    userInterestService.addInterest(1L, 1L);
    
    // 获取推荐
    List<EBook> recommendations = userInterestService.getInterestBasedRecommendations(1L, 10);
    assertNotNull(recommendations);
    assertTrue(recommendations.size() > 0);
    
    // 验证推荐书籍属于用户兴趣分类
    for (EBook book : recommendations) {
        assertEquals(1L, book.getCategoryId());
    }
}
```

### 2. 冷启动测试

```java
@Test
void testColdStartRecommendations() {
    // 新用户未设置兴趣
    List<EBook> recommendations = recommendationService.getPersonalRecommendations(999L, 10);
    
    // 应返回热门书籍
    assertNotNull(recommendations);
    assertTrue(recommendations.size() > 0);
}
```

## 数据示例

### 示例数据插入

```sql
-- 用户1选择了计算机和文学分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(1, 1),  -- 计算机与互联网
(1, 2);  -- 文学小说

-- 用户2选择了计算机、经济和教育分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(2, 1),  -- 计算机与互联网
(2, 3),  -- 经济管理
(2, 4);  -- 教育学习
```

## 未来扩展

### 1. 智能兴趣推荐
- 根据用户浏览和购买行为，自动推荐可能感兴趣的分类
- 用户可一键添加系统推荐的兴趣标签

### 2. 兴趣权重
- 为不同兴趣标签设置权重（主要兴趣、次要兴趣）
- 推荐算法根据权重调整推荐比例

### 3. 兴趣标签细化
- 支持二级分类兴趣选择（如"Java"、"Python"等）
- 更精准的推荐结果

### 4. 兴趣社交
- 查看其他用户的兴趣标签
- 关注兴趣相似的用户
- 兴趣社区功能

## 总结

用户兴趣管理功能是 IntelliBook-Mall 推荐系统的核心创新点，通过让用户主动表达兴趣偏好，有效解决了推荐系统的冷启动问题，提升了推荐准确度和用户满意度。该功能设计简洁、易用，与现有推荐算法完美融合，为用户提供了更好的个性化体验。
