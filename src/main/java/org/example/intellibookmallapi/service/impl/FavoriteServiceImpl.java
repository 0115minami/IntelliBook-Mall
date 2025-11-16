package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.FavoriteVO;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.entity.Favorite;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.mapper.FavoriteMapper;
import org.example.intellibookmallapi.service.FavoriteService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏Service实现类
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Autowired
    private EBookMapper eBookMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long userId, Long bookId) {
        // 1. 验证书籍是否存在
        EBook book = eBookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        if (book.getStatus() == null || book.getStatus() == 0) {
            throw new BusinessException("书籍已下架");
        }
        
        // 2. 检查是否已收藏
        Favorite existingFavorite = favoriteMapper.selectByUserIdAndBookId(userId, bookId);
        if (existingFavorite != null) {
            throw new BusinessException("该书籍已在收藏夹中");
        }
        
        // 3. 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBookId(bookId);
        
        int result = favoriteMapper.insert(favorite);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long userId, Long bookId) {
        int result = favoriteMapper.deleteByUserIdAndBookId(userId, bookId);
        if (result == 0) {
            throw new BusinessException("收藏不存在");
        }
        return true;
    }
    
    @Override
    public PageResult<FavoriteVO> getFavoriteList(Long userId, Integer page, Integer pageSize) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }
        
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询收藏列表
        List<FavoriteVO> list = favoriteMapper.selectFavoriteListByUserId(userId, offset, pageSize);
        
        // 查询总数
        int total = favoriteMapper.countByUserId(userId);
        
        // 构建分页结果
        PageResult<FavoriteVO> pageResult = new PageResult<>();
        pageResult.setList(list);
        pageResult.setTotalCount((long) total);
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages((int) Math.ceil((double) total / pageSize));
        pageResult.setHasPrevious(page > 1);
        pageResult.setHasNext(page < pageResult.getTotalPages());
        
        return pageResult;
    }
    
    @Override
    public boolean checkFavorite(Long userId, Long bookId) {
        int count = favoriteMapper.checkFavorite(userId, bookId);
        return count > 0;
    }
}
