package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.EBookSearchParam;
import org.example.intellibookmallapi.dto.EBookVO;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.service.EBookService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 电子书业务实现类
 */
@Service
public class EBookServiceImpl implements EBookService {
    
    @Autowired
    private EBookMapper ebookMapper;
    
    @Override
    public EBookVO getEBookById(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        
        EBook ebook = ebookMapper.selectByPrimaryKey(bookId);
        if (ebook == null) {
            throw new BusinessException("书籍不存在或已下架");
        }
        
        return EBookVO.fromEntity(ebook);
    }
    
    @Override
    public PageResult<EBookVO> searchEBooks(EBookSearchParam searchParam) {
        // 参数校验
        if (searchParam == null) {
            searchParam = new EBookSearchParam();
        }
        
        // 验证并设置默认值
        if (searchParam.getPageNum() == null || searchParam.getPageNum() < 1) {
            searchParam.setPageNum(1);
        }
        if (searchParam.getPageSize() == null || searchParam.getPageSize() < 1) {
            searchParam.setPageSize(20);
        }
        if (searchParam.getPageSize() > 100) {
            searchParam.setPageSize(100); // 限制最大每页数量
        }
        
        // 验证排序参数
        if (!searchParam.isValidSortBy()) {
            searchParam.setSortBy("create_time_desc");
        }
        
        // 查询总数
        Long totalCount = ebookMapper.countSearchEBooks(searchParam);
        
        // 如果没有结果，直接返回空分页
        if (totalCount == 0) {
            return PageResult.empty(searchParam.getPageNum(), searchParam.getPageSize());
        }
        
        // 查询数据列表
        List<EBook> ebooks = ebookMapper.searchEBooks(searchParam);
        
        // 转换为VO
        List<EBookVO> voList = ebooks.stream()
                .map(EBookVO::fromEntity)
                .collect(Collectors.toList());
        
        return PageResult.of(searchParam.getPageNum(), searchParam.getPageSize(), totalCount, voList);
    }
    
    @Override
    public List<EBookVO> getPopularEBooks(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (limit > 50) {
            limit = 50; // 限制最大数量
        }
        
        List<EBook> ebooks = ebookMapper.selectPopularEBooks(limit);
        
        return ebooks.stream()
                .map(EBookVO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EBookVO> getLatestEBooks(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (limit > 50) {
            limit = 50; // 限制最大数量
        }
        
        List<EBook> ebooks = ebookMapper.selectLatestEBooks(limit);
        
        return ebooks.stream()
                .map(EBookVO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public PageResult<EBookVO> getEBooksByCategory(Long categoryId, Integer pageNum, Integer pageSize) {
        if (categoryId == null || categoryId <= 0) {
            throw new BusinessException("分类ID不能为空");
        }
        
        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        
        // 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        
        // 查询总数
        Long totalCount = ebookMapper.countEBooksByCategory(categoryId);
        
        // 如果没有结果，直接返回空分页
        if (totalCount == 0) {
            return PageResult.empty(pageNum, pageSize);
        }
        
        // 查询数据列表
        List<EBook> ebooks = ebookMapper.selectEBooksByCategory(categoryId, offset, pageSize);
        
        // 转换为VO
        List<EBookVO> voList = ebooks.stream()
                .map(EBookVO::fromEntity)
                .collect(Collectors.toList());
        
        return PageResult.of(pageNum, pageSize, totalCount, voList);
    }
    
    @Override
    public List<EBookVO> getRelatedEBooks(Long bookId, Integer limit) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        
        if (limit == null || limit <= 0) {
            limit = 5;
        }
        if (limit > 20) {
            limit = 20;
        }
        
        // 先获取当前书籍信息
        EBook currentBook = ebookMapper.selectByPrimaryKey(bookId);
        if (currentBook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 根据标签查找相关书籍
        List<EBook> relatedBooks = null;
        if (currentBook.getTags() != null && !currentBook.getTags().trim().isEmpty()) {
            String[] tags = currentBook.getTagArray();
            if (tags.length > 0) {
                // 使用第一个标签查找相关书籍
                relatedBooks = ebookMapper.selectEBooksByTag(tags[0], bookId, limit);
            }
        }
        
        // 如果通过标签没找到足够的相关书籍，则使用同分类的书籍补充
        if (relatedBooks == null || relatedBooks.size() < limit) {
            int remaining = limit - (relatedBooks == null ? 0 : relatedBooks.size());
            List<EBook> categoryBooks = ebookMapper.selectEBooksByCategory(
                    currentBook.getCategoryId(), 0, remaining);
            
            if (relatedBooks == null) {
                relatedBooks = categoryBooks;
            } else {
                // 合并结果，去重
                for (EBook book : categoryBooks) {
                    if (!book.getBookId().equals(bookId) && 
                        relatedBooks.stream().noneMatch(b -> b.getBookId().equals(book.getBookId()))) {
                        relatedBooks.add(book);
                        if (relatedBooks.size() >= limit) {
                            break;
                        }
                    }
                }
            }
        }
        
        return relatedBooks.stream()
                .map(EBookVO::fromEntity)
                .collect(Collectors.toList());
    }
}
