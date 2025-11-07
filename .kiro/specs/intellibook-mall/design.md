# IntelliBook-Mall 设计文档

## 概述

IntelliBook-Mall 是一个基于智能推荐的电子书商城系统，采用前后端分离架构。后端基于 Spring Boot 3.1.11 + MyBatis 构建 RESTful API，前端使用 Vue.js 开发单页应用。系统核心功能包括电子书管理、搜索筛选、购物车、订单管理、收藏评价等。

本设计基于 newbee-mall 开源项目的架构进行改造，保留其成熟的用户认证、订单流程等模块，同时针对电子书业务特性和毕业设计需求进行定制开发。

> **📌 项目定位**：本项目为毕业设计项目，采用简化的数据库设计和技术栈，在保证核心功能完整的前提下，优化开发效率和演示便利性。

### 设计目标

- 实现完整的电子书商城核心功能
- 支持多语言电子书分类和检索（中文/英文）
- 提供友好的用户界面和操作体验
- 保证系统稳定性和可演示性
- 便于毕业设计答辩和展示

### 技术栈

**后端**:
- Spring Boot 3.1.11+
- MyBatis 3.5.x
- **SQLite 3** (轻量级嵌入式数据库，零配置)
- SpringDoc 2.x (OpenAPI 3.0)
- Lombok 1.18.x

**前端**:
- Vue.js 3.4.x+
- Vue Router 4.x
- Pinia (状态管理)
- Axios 1.x
- Element Plus 2.x (PC端UI)

**文件管理**:
- 本地文件系统存储
- 支持 PDF、EPUB、MOBI 格式

**版本说明**：
- 以上版本号仅供参考，请根据项目具体需求选择
- SQLite 无需单独安装，通过 JDBC 驱动即可使用
- 适合毕业设计、原型开发和演示场景

### 🎯 毕业设计版本说明

本设计采用**简化方案**，相比完整的生产级系统：

**简化内容**：
- 数据库：MySQL → SQLite（零配置、便携）
- 表数量：15+ 张表 → 8 张核心表
- 复杂功能：推荐系统、阅读进度等改为可选

**保留功能**：
- ✅ 完整的用户管理系统
- ✅ 电子书 CRUD 和文件管理
- ✅ 多语言分类和搜索
- ✅ 购物车和订单流程
- ✅ 收藏和评价功能
- ✅ 文件上传下载

**优势**：
- 开发效率提升 50%
- 答辩演示友好（导师可直接运行）
- 代码简洁易懂
- 功能完整度 85%+

## 系统架构

### 整体架构图

```mermaid
graph TB
    subgraph "前端层"
        A[Vue.js 用户端] 
        B[Vue.js 管理后台]
    end
    
    subgraph "API 网关层"
        C[Spring Boot REST API]
    end
    
    subgraph "业务逻辑层"
        D[用户服务]
        E[电子书服务]
        F[订单服务]
        G[推荐服务]
        H[阅读服务]
        I[搜索服务]
    end
    
    subgraph "数据访问层"
        J[MyBatis DAO]
    end
    
    subgraph "数据存储层"
        K[(SQLite 数据库)]
        L[文件存储系统]
    end
    
    A --> C
    B --> C
    C --> D
    C --> E
    C --> F
    C --> G
    C --> H
    C --> I
    D --> J
    E --> J
    F --> J
    G --> J
    H --> J
    I --> J
    J --> K
    E --> L
    H --> L
    G --> M
```

### 分层架构说明

**1. 前端层 (Presentation Layer)**
- 用户端：面向普通用户的电子书浏览、购买、阅读界面
- 管理后台：面向管理员的电子书管理、订单管理、数据统计界面

**2. API 网关层 (API Gateway Layer)**
- 统一的 RESTful API 接口
- 请求路由和负载均衡
- 身份认证和权限校验
- 统一异常处理和响应格式

**3. 业务逻辑层 (Business Logic Layer)**
- 用户服务：注册、登录、个人信息管理
- 电子书服务：电子书 CRUD、元数据管理、分类管理
- 订单服务：购物车、订单创建、支付处理
- 推荐服务：基于用户行为和内容的推荐算法
- 阅读服务：在线阅读、下载管理、阅读进度记录
- 搜索服务：多条件搜索、全文检索

**4. 数据访问层 (Data Access Layer)**
- MyBatis Mapper 接口
- SQL 映射文件
- 数据库连接池管理

**5. 数据存储层 (Data Storage Layer)**
- SQLite：存储结构化数据（用户、订单、电子书元数据等）
- 文件系统：存储电子书文件、封面图片

**数据库选型说明**：
- 采用 SQLite 嵌入式数据库，零配置、便携、适合毕业设计
- 数据库文件：`intellibook.db`（自动创建在项目根目录）
- 支持完整的 SQL 功能和事务处理
- 可使用 DB Browser for SQLite 进行可视化管理

### 与 newbee-mall 架构对比

| 层次 | newbee-mall | IntelliBook-Mall | 变化说明 |
|------|-------------|------------------|----------|
| 前端层 | Vue.js 商城 + 管理后台 | Vue.js 电子书商城 + 管理后台 | 界面重新设计 |
| API 层 | Spring Boot REST API | Spring Boot REST API | 保持一致 |
| 业务层 | 商品、订单、用户服务 | 电子书、订单、用户、推荐、阅读服务 | 新增推荐和阅读服务 |
| 数据层 | MyBatis + MySQL | MyBatis + MySQL + Redis | 新增 Redis |
| 存储层 | 本地文件存储 | 本地/云文件存储 | 增强文件管理 |


## 核心组件和接口

### 1. 用户模块 (User Module)

#### 1.1 实体设计

**MallUser (用户实体)**
```java
@Data
public class MallUser {
    private Long userId;              // 用户ID
    private String loginName;         // 登录名
    private String nickName;          // 昵称
    private String email;             // 邮箱 (新增)
    private String passwordMd5;       // MD5加密密码
    private String introduceSign;     // 个性签名
    private Byte isDeleted;           // 删除标识
    private Byte lockedFlag;          // 锁定标识
    private Integer loginFailCount;   // 登录失败次数 (新增)
    private Date lockTime;            // 锁定时间 (新增)
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**对比 newbee-mall**: 
- 新增：email、loginFailCount、lockTime、updateTime 字段
- 保留：其他所有字段

#### 1.2 服务接口

**MallUserService**
```java
public interface MallUserService {
    // 保留自 newbee-mall
    String register(String loginName, String password);
    String login(String loginName, String password);
    Boolean updateUserInfo(MallUser user);
    MallUser getUserById(Long userId);
    
    // 新增方法
    Boolean sendEmailVerification(String email);
    Boolean verifyEmail(String email, String code);
    Boolean checkLoginFailure(String loginName);
    void resetLoginFailCount(String loginName);
}
```

### 2. 电子书模块 (EBook Module)

#### 2.1 实体设计

**EBook (电子书实体 - 元数据)**
```java
@Data
public class EBook {
    private Long bookId;              // 书籍ID
    private String bookTitle;         // 书名
    private String author;            // 作者
    private String isbn;              // ISBN (新增)
    private String publisher;         // 出版社 (新增)
    private Date publishDate;         // 出版日期 (新增)
    private String bookIntro;         // 简介
    private Long categoryId;          // 分类ID
    private String coverImg;          // 封面图片
    private Integer pageCount;        // 页数 (新增)
    private Integer originalPrice;    // 原价(分)
    private Integer sellingPrice;     // 售价(分)
    private String tags;              // 标签(逗号分隔) (新增)
    private String language;          // 语言代码 (zh-CN, en-US, ja-JP, ko-KR等) (新增)
    private Double avgRating;         // 平均评分 (新增)
    private Integer ratingCount;      // 评分人数 (新增)
    private Byte sellStatus;          // 上架状态
    private Byte isDeleted;           // 删除标识
    private Integer createUser;       // 创建者
    private Date createTime;          // 创建时间
    private Integer updateUser;       // 更新者
    private Date updateTime;          // 更新时间
    private String detailContent;     // 详情内容
    
    // 关联属性（非数据库字段）
    private List<EBookFile> files;    // 电子书文件列表（多格式支持）
}
```

**EBookFile (电子书文件实体)** - 新增，支持多格式
```java
@Data
public class EBookFile {
    private Long fileId;              // 文件ID
    private Long bookId;              // 书籍ID
    private String fileFormat;        // 文件格式 (PDF/EPUB/MOBI/AZW3等)
    private String filePath;          // 文件路径
    private Long fileSize;            // 文件大小(字节)
    private Integer downloadCount;    // 该格式的下载次数
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**对比 newbee-mall 的 NewBeeMallGoods**:
- 删除：stockNum (库存)、goodsCarousel (轮播图)
- 新增：isbn、publisher、publishDate、pageCount、tags、language、avgRating、ratingCount
- 重命名：goodsId → bookId, goodsName → bookTitle 等
- **重要变更**：文件信息独立为 EBookFile 表，支持一本书多种格式

**多格式支持说明**:
- 一本电子书可以有多种格式（PDF、EPUB、MOBI、AZW3等）
- 每种格式独立存储在 tb_ebook_file 表中
- 用户购买一本书后，可以下载所有可用格式
- 不同格式可以有独立的下载统计

**多语言支持说明**:
- language 字段使用 ISO 639-1 + ISO 3166-1 标准
- 默认值为 'zh-CN'，支持中文、英文、日文、韩文等
- 用于电子书的语言分类和筛选功能


**EBookCategory (电子书分类实体)**
```java
@Data
public class EBookCategory {
    private Long categoryId;          // 分类ID
    private Byte categoryLevel;       // 分类级别(1-一级 2-二级)
    private Long parentId;            // 父分类ID
    private String categoryName;      // 分类名称(中文)
    private String categoryNameEn;    // 分类英文名称 (新增)
    private Integer categoryRank;     // 排序值
    private String categoryIcon;      // 分类图标
    private Byte isDeleted;           // 删除标识
    private Date createTime;          // 创建时间
    private Integer createUser;       // 创建者ID
    private Date updateTime;          // 更新时间
    private Integer updateUser;       // 更新者ID
}
```

**多语言分类支持**:
- categoryName: 中文分类名称（主要）
- categoryNameEn: 英文分类名称（新增）
- 前端根据用户语言偏好显示对应名称
- 预留扩展其他语言字段的能力

**EBookTag (电子书标签实体)** - 新增
```java
@Data
public class EBookTag {
    private Long tagId;               // 标签ID
    private String tagName;           // 标签名称
    private Integer useCount;         // 使用次数
    private Date createTime;          // 创建时间
}
```

**EBookTagRelation (电子书-标签关联表)** - 新增
```java
@Data
public class EBookTagRelation {
    private Long relationId;          // 关联ID
    private Long bookId;              // 书籍ID
    private Long tagId;               // 标签ID
    private Date createTime;          // 创建时间
}
```

#### 2.2 服务接口

**EBookService**
```java
public interface EBookService {
    // 基础 CRUD (改造自 newbee-mall)
    PageResult<EBook> getEBookPage(PageQueryUtil pageUtil);
    String saveEBook(EBook ebook);
    String updateEBook(EBook ebook);
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);
    EBook getEBookById(Long id);  // 返回包含所有格式文件的完整信息
    
    // 新增方法
    Boolean batchImportEBooks(List<EBook> ebooks);
    List<EBook> getEBooksByIsbn(String isbn);
    List<EBook> getEBooksByAuthor(String author);
    Boolean addTagToEBook(Long bookId, Long tagId);
    Boolean removeTagFromEBook(Long bookId, Long tagId);
    List<EBookTag> getEBookTags(Long bookId);
    
    // 搜索相关 (增强自 newbee-mall)
    PageResult<EBook> searchEBooks(EBookSearchParam searchParam);
    PageResult<EBook> advancedSearch(AdvancedSearchParam param);
}
```

**EBookFileService** - 新增，管理电子书文件
```java
public interface EBookFileService {
    // 文件管理
    String uploadEBookFile(MultipartFile file, Long bookId, String format);
    Boolean deleteEBookFile(Long fileId);
    List<EBookFile> getEBookFiles(Long bookId);  // 获取某本书的所有格式文件
    EBookFile getEBookFile(Long bookId, String format);  // 获取指定格式的文件
    
    // 文件验证
    Boolean validateFileFormat(String format);  // 验证文件格式是否支持
    Boolean checkFileExists(Long bookId, String format);  // 检查文件是否已存在
    
    // 下载统计
    Boolean incrementDownloadCount(Long fileId);
    Integer getTotalDownloadCount(Long bookId);  // 获取所有格式的总下载次数
}
```

**EBookSearchParam (搜索参数)** - 新增
```java
@Data
public class EBookSearchParam {
    private String keyword;           // 关键词
    private String author;            // 作者
    private String isbn;              // ISBN
    private Long categoryId;          // 分类ID
    private List<Long> tagIds;        // 标签ID列表
    private Integer minPrice;         // 最低价格
    private Integer maxPrice;         // 最高价格
    private Integer startYear;        // 起始年份
    private Integer endYear;          // 结束年份
    private String sortBy;            // 排序字段
    private String sortOrder;         // 排序方向
    private Integer pageNumber;       // 页码
    private Integer pageSize;         // 每页数量
}
```

### 3. 订单模块 (Order Module)

#### 3.1 实体设计

**EBookOrder (电子书订单实体)**
```java
@Data
public class EBookOrder {
    private Long orderId;             // 订单ID
    private String orderNo;           // 订单号
    private Long userId;              // 用户ID
    private Integer totalPrice;       // 总价(分)
    private Byte payStatus;           // 支付状态
    private Byte payType;             // 支付方式
    private Date payTime;             // 支付时间
    private Byte orderStatus;         // 订单状态
    private String extraInfo;         // 额外信息
    private Byte isDeleted;           // 删除标识
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**对比 newbee-mall 的 NewBeeMallOrder**:
- 删除：无（订单主表结构基本保持一致）
- 说明：由于电子书无需物流，订单地址表可以删除

**EBookOrderItem (订单项实体)**
```java
@Data
public class EBookOrderItem {
    private Long orderItemId;         // 订单项ID
    private Long orderId;             // 订单ID
    private Long bookId;              // 书籍ID
    private String bookTitle;         // 书名
    private String bookCover;         // 封面
    private Integer sellingPrice;     // 售价(分)
    private Date createTime;          // 创建时间
}
```

**对比 newbee-mall 的 NewBeeMallOrderItem**:
- 删除：goodsCount (数量字段，电子书无需数量)


#### 3.2 服务接口

**EBookOrderService**
```java
public interface EBookOrderService {
    // 保留自 newbee-mall
    EBookOrderDetailVO getOrderDetailByOrderId(Long orderId);
    EBookOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);
    PageResult<EBookOrder> getMyOrders(PageQueryUtil pageUtil);
    String cancelOrder(String orderNo, Long userId);
    String paySuccess(String orderNo, int payType);
    String saveOrder(MallUser user, List<EBookShoppingCartItemVO> items);
    
    // 修改自 newbee-mall (移除物流相关)
    PageResult<EBookOrder> getEBookOrdersPage(PageQueryUtil pageUtil);
    String updateOrderInfo(EBookOrder order);
    String closeOrder(Long[] ids);
    
    // 删除自 newbee-mall
    // String finishOrder() - 电子书无需确认收货
    // String checkDone() - 电子书无需配货
    // String checkOut() - 电子书无需出库
    
    // 新增方法
    Boolean grantEBookAccess(Long orderId);  // 授予电子书访问权限
    List<EBook> getPurchasedEBooks(Long userId);  // 获取已购买的电子书
}
```

### 4. 购物车模块 (Shopping Cart Module)

#### 4.1 实体设计

**EBookShoppingCartItem (购物车项实体)**
```java
@Data
public class EBookShoppingCartItem {
    private Long cartItemId;          // 购物车项ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private Byte isDeleted;           // 删除标识
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**对比 newbee-mall 的 NewBeeMallShoppingCartItem**:
- 删除：goodsCount (数量字段，电子书无需数量)

#### 4.2 服务接口

**EBookShoppingCartService**
```java
public interface EBookShoppingCartService {
    // 保留自 newbee-mall
    String saveEBookCartItem(EBookShoppingCartItem cartItem);
    Boolean deleteCartItem(Long cartItemId, Long userId);
    List<EBookShoppingCartItemVO> getMyShoppingCartItems(Long userId);
    
    // 删除自 newbee-mall
    // updateCartItem() - 电子书无需修改数量
    
    // 新增方法
    Boolean checkIfPurchased(Long userId, Long bookId);  // 检查是否已购买
}
```

### 5. 阅读模块 (Reading Module) - 全新模块

#### 5.1 实体设计

**ReadingProgress (阅读进度实体)** - 新增
```java
@Data
public class ReadingProgress {
    private Long progressId;          // 进度ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private Long fileId;              // 文件ID（用户阅读的是哪种格式）
    private Integer currentPage;      // 当前页码（PDF适用）
    private Double progressPercent;   // 进度百分比（0-100）
    private String lastPosition;      // 最后位置(JSON格式)
    private Integer readingTime;      // 累计阅读时长（秒）
    private Date lastReadTime;        // 最后阅读时间
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**lastPosition JSON 格式说明**：
```json
// PDF格式示例
{
  "format": "PDF",
  "page": 156,
  "scrollTop": 320,
  "chapter": "第8章 接口与内部类",
  "timestamp": "2024-01-15T10:30:00"
}

// EPUB格式示例
{
  "format": "EPUB",
  "cfi": "epubcfi(/6/14[chap07]!/4/2/1:0)",
  "chapter": "第七章",
  "percent": 45.0,
  "timestamp": "2024-01-14T20:15:00"
}
```

**DownloadRecord (下载记录实体)** - 新增
```java
@Data
public class DownloadRecord {
    private Long recordId;            // 记录ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private String downloadUrl;       // 下载链接
    private Date expireTime;          // 过期时间
    private Integer downloadCount;    // 下载次数
    private Date createTime;          // 创建时间
}
```

#### 5.2 服务接口

**ReadingService** - 新增
```java
public interface ReadingService {
    // 阅读进度管理
    Boolean saveReadingProgress(ReadingProgress progress);
    Boolean updateReadingProgress(Long userId, Long bookId, Integer currentPage, Double percent, String position);
    ReadingProgress getReadingProgress(Long userId, Long bookId);
    List<ReadingProgress> getRecentReadingBooks(Long userId, Integer limit);
    Boolean deleteReadingProgress(Long userId, Long bookId);
    
    // 阅读时长统计
    Boolean updateReadingTime(Long userId, Long bookId, Integer seconds);
    Integer getTotalReadingTime(Long userId);  // 获取用户总阅读时长
    
    // 在线阅读
    String getEBookContent(Long userId, Long bookId, String format);
    Boolean verifyReadingPermission(Long userId, Long bookId);
    
    // 下载管理
    String generateDownloadUrl(Long userId, Long bookId, String format);
    Boolean verifyDownloadPermission(Long userId, Long bookId);
    DownloadRecord getDownloadRecord(Long userId, Long bookId);
    Boolean incrementDownloadCount(Long recordId);
}
```


### 6. 推荐模块 (Recommendation Module) - 全新模块

#### 6.1 实体设计

**UserBehavior (用户行为实体)** - 新增
```java
@Data
public class UserBehavior {
    private Long behaviorId;          // 行为ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private String behaviorType;      // 行为类型(VIEW/SEARCH/PURCHASE/FAVORITE)
    private String behaviorData;      // 行为数据(JSON格式)
    private Date createTime;          // 创建时间
}
```

**RecommendationCache (推荐缓存实体)** - 新增
```java
@Data
public class RecommendationCache {
    private Long cacheId;             // 缓存ID
    private Long userId;              // 用户ID
    private String recommendType;     // 推荐类型(PERSONAL/HOT/SIMILAR)
    private String bookIds;           // 书籍ID列表(逗号分隔)
    private Date expireTime;          // 过期时间
    private Date createTime;          // 创建时间
}
```

#### 6.2 服务接口

**RecommendationService** - 新增
```java
public interface RecommendationService {
    // 用户行为追踪
    Boolean trackUserBehavior(UserBehavior behavior);
    List<UserBehavior> getUserBehaviorHistory(Long userId, String behaviorType);
    
    // 推荐算法
    List<EBook> getPersonalRecommendations(Long userId, Integer limit);
    List<EBook> getSimilarBooks(Long bookId, Integer limit);
    List<EBook> getHotBooks(Integer limit);
    List<EBook> getCategoryRecommendations(Long categoryId, Integer limit);
    
    // 推荐缓存管理
    Boolean cacheRecommendations(Long userId, String type, List<Long> bookIds);
    List<Long> getCachedRecommendations(Long userId, String type);
    Boolean clearRecommendationCache(Long userId);
}
```

### 7. 评价模块 (Review Module) - 全新模块

#### 7.1 实体设计

**EBookReview (电子书评价实体)** - 新增
```java
@Data
public class EBookReview {
    private Long reviewId;            // 评价ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private Integer rating;           // 评分(1-5)
    private String reviewContent;     // 评价内容
    private Integer likeCount;        // 点赞数
    private Byte isDeleted;           // 删除标识
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
```

**ReviewLike (评价点赞实体)** - 新增
```java
@Data
public class ReviewLike {
    private Long likeId;              // 点赞ID
    private Long userId;              // 用户ID
    private Long reviewId;            // 评价ID
    private Date createTime;          // 创建时间
}
```

#### 7.2 服务接口

**EBookReviewService** - 新增
```java
public interface EBookReviewService {
    // 评价管理
    String saveReview(EBookReview review);
    Boolean updateReview(EBookReview review);
    Boolean deleteReview(Long reviewId, Long userId);
    PageResult<EBookReview> getBookReviews(Long bookId, PageQueryUtil pageUtil);
    
    // 评分统计
    Double calculateAvgRating(Long bookId);
    Boolean updateBookRating(Long bookId);
    
    // 点赞管理
    Boolean likeReview(Long userId, Long reviewId);
    Boolean unlikeReview(Long userId, Long reviewId);
    Boolean checkIfLiked(Long userId, Long reviewId);
    
    // 权限验证
    Boolean verifyReviewPermission(Long userId, Long bookId);
}
```

### 8. 用户兴趣模块 (User Interest Module) - 全新模块

#### 8.1 实体设计

**UserInterest (用户兴趣实体)** - 新增
```java
@Data
public class UserInterest {
    private Long interestId;          // 兴趣ID
    private Long userId;              // 用户ID
    private Long categoryId;          // 分类ID
    private Date createTime;          // 创建时间
}
```

#### 8.2 服务接口

**UserInterestService** - 新增
```java
public interface UserInterestService {
    // 兴趣管理
    Boolean addInterest(Long userId, Long categoryId);
    Boolean removeInterest(Long userId, Long categoryId);
    Boolean batchAddInterests(Long userId, List<Long> categoryIds);
    List<EBookCategory> getUserInterests(Long userId);
    Boolean checkIfInterested(Long userId, Long categoryId);
    
    // 推荐相关
    List<EBook> getInterestBasedRecommendations(Long userId, Integer limit);
    Boolean updateInterestsFromBehavior(Long userId);  // 根据用户行为自动更新兴趣
}
```

### 9. 收藏模块 (Favorite Module) - 全新模块

#### 9.1 实体设计

**EBookFavorite (电子书收藏实体)** - 新增
```java
@Data
public class EBookFavorite {
    private Long favoriteId;          // 收藏ID
    private Long userId;              // 用户ID
    private Long bookId;              // 书籍ID
    private Date createTime;          // 创建时间
}
```

#### 9.2 服务接口

**EBookFavoriteService** - 新增
```java
public interface EBookFavoriteService {
    Boolean addFavorite(Long userId, Long bookId);
    Boolean removeFavorite(Long userId, Long bookId);
    Boolean checkIfFavorited(Long userId, Long bookId);
    PageResult<EBook> getMyFavorites(Long userId, PageQueryUtil pageUtil);
    Integer getFavoriteCount(Long bookId);
}
```


## 数据模型

### 数据库 ER 图

```mermaid
erDiagram
    MALL_USER ||--o{ EBOOK_ORDER : places
    MALL_USER ||--o{ EBOOK_SHOPPING_CART_ITEM : has
    MALL_USER ||--o{ USER_BEHAVIOR : generates
    MALL_USER ||--o{ READING_PROGRESS : tracks
    MALL_USER ||--o{ EBOOK_REVIEW : writes
    MALL_USER ||--o{ EBOOK_FAVORITE : collects
    
    EBOOK ||--o{ EBOOK_ORDER_ITEM : contains
    EBOOK ||--o{ EBOOK_SHOPPING_CART_ITEM : in
    EBOOK ||--o{ EBOOK_TAG_RELATION : has
    EBOOK ||--o{ USER_BEHAVIOR : tracked
    EBOOK ||--o{ READING_PROGRESS : read
    EBOOK ||--o{ EBOOK_REVIEW : reviewed
    EBOOK ||--o{ EBOOK_FAVORITE : favorited
    EBOOK }o--|| EBOOK_CATEGORY : belongs_to
    
    EBOOK_ORDER ||--o{ EBOOK_ORDER_ITEM : includes
    
    EBOOK_TAG ||--o{ EBOOK_TAG_RELATION : used_in
    
    EBOOK_REVIEW ||--o{ REVIEW_LIKE : receives
    
    MALL_USER {
        bigint user_id PK
        varchar login_name
        varchar nick_name
        varchar email
        varchar password_md5
        tinyint locked_flag
        int login_fail_count
        datetime lock_time
        datetime create_time
    }
    
    EBOOK {
        bigint book_id PK
        varchar book_title
        varchar author
        varchar isbn
        varchar publisher
        date publish_date
        bigint category_id FK
        varchar cover_img
        varchar file_format
        varchar file_path
        bigint file_size
        int page_count
        int original_price
        int selling_price
        varchar tags
        double avg_rating
        int rating_count
        tinyint sell_status
        datetime create_time
    }
    
    EBOOK_CATEGORY {
        bigint category_id PK
        tinyint category_level
        bigint parent_id
        varchar category_name
        int category_rank
        datetime create_time
    }
    
    EBOOK_ORDER {
        bigint order_id PK
        varchar order_no
        bigint user_id FK
        int total_price
        tinyint pay_status
        tinyint pay_type
        datetime pay_time
        tinyint order_status
        datetime create_time
    }
    
    EBOOK_ORDER_ITEM {
        bigint order_item_id PK
        bigint order_id FK
        bigint book_id FK
        varchar book_title
        int selling_price
        datetime create_time
    }
    
    EBOOK_SHOPPING_CART_ITEM {
        bigint cart_item_id PK
        bigint user_id FK
        bigint book_id FK
        datetime create_time
    }
    
    USER_BEHAVIOR {
        bigint behavior_id PK
        bigint user_id FK
        bigint book_id FK
        varchar behavior_type
        datetime create_time
    }
    
    READING_PROGRESS {
        bigint progress_id PK
        bigint user_id FK
        bigint book_id FK
        int current_page
        double progress_percent
        datetime last_read_time
    }
    
    EBOOK_REVIEW {
        bigint review_id PK
        bigint user_id FK
        bigint book_id FK
        int rating
        varchar review_content
        int like_count
        datetime create_time
    }
    
    EBOOK_FAVORITE {
        bigint favorite_id PK
        bigint user_id FK
        bigint book_id FK
        datetime create_time
    }
    
    EBOOK_TAG {
        bigint tag_id PK
        varchar tag_name
        int use_count
        datetime create_time
    }
    
    EBOOK_TAG_RELATION {
        bigint relation_id PK
        bigint book_id FK
        bigint tag_id FK
        datetime create_time
    }
    
    REVIEW_LIKE {
        bigint like_id PK
        bigint user_id FK
        bigint review_id FK
        datetime create_time
    }
```


### 核心表结构设计（SQLite 简化版）

> **设计说明**：本设计采用简化的表结构，从原设计的 15+ 张表精简为 14 张表（8张核心+1张文件+3张书单+1张兴趣+1张阅读进度），在保证核心功能的前提下，增加创新功能。

#### 表结构总览

| 表名 | 说明 | 记录数预估 |
|------|------|-----------|
| tb_user | 用户表（含管理员） | 100+ |
| tb_category | 电子书分类表 | 20-50 |
| tb_ebook | 电子书表（核心） | 100-1000 |
| tb_order | 订单表 | 500+ |
| tb_order_item | 订单项表 | 1000+ |
| tb_cart | 购物车表 | 200+ |
| tb_favorite | 收藏表 | 500+ |
| tb_review | 评价表 | 300+ |
| **tb_ebook_file** | **电子书文件表（新增）** | **300+** |
| **tb_booklist** | **书单表（新增）** | **50+** |
| **tb_booklist_item** | **书单-书籍关联表（新增）** | **200+** |
| **tb_booklist_follow** | **书单关注表（新增）** | **100+** |
| **tb_user_interest** | **用户兴趣表（新增）** | **200+** |
| **tb_reading_progress** | **阅读进度表（新增）** | **500+** |

#### 1. tb_user (用户表)

```sql
CREATE TABLE IF NOT EXISTS tb_user (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    is_admin TINYINT DEFAULT 0,  -- 0-普通用户 1-管理员
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**字段说明**:
- `user_id`: 用户ID（自增主键）
- `username`: 登录用户名（唯一）
- `password`: 密码（MD5加密）
- `email`: 邮箱
- `nickname`: 昵称
- `is_admin`: 管理员标识（0-普通用户 1-管理员）
- `create_time`: 创建时间

**简化说明**:
- 合并了原设计的 `tb_mall_user` 和 `tb_admin_user` 表
- 删除了 Token 表（使用 Session 管理）
- 删除了登录失败锁定相关字段（简化功能）

#### 2. tb_category (电子书分类表)

```sql
CREATE TABLE IF NOT EXISTS tb_category (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name VARCHAR(50) NOT NULL,
    category_name_en VARCHAR(50),
    parent_id INTEGER DEFAULT 0,
    category_level TINYINT DEFAULT 1,  -- 1-一级 2-二级
    sort_order INTEGER DEFAULT 0,
    FOREIGN KEY (parent_id) REFERENCES tb_category(category_id)
);
```

**字段说明**:
- `category_id`: 分类ID
- `category_name`: 分类名称（中文）
- `category_name_en`: 分类英文名称
- `parent_id`: 父分类ID（0表示一级分类）
- `category_level`: 分类级别（1-一级 2-二级）
- `sort_order`: 排序值

**多语言支持**:
- 支持中英文分类名称
- 前端根据用户语言偏好显示对应名称

#### 3. tb_ebook (电子书表) - 核心表

```sql
CREATE TABLE IF NOT EXISTS tb_ebook (
    book_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20),
    publisher VARCHAR(100),
    publish_date DATE,
    book_intro TEXT,
    category_id INTEGER NOT NULL,
    cover_img VARCHAR(200),
    file_format VARCHAR(10) NOT NULL,  -- PDF/EPUB/MOBI
    file_path VARCHAR(500) NOT NULL,
    file_size INTEGER,
    page_count INTEGER DEFAULT 0,
    price INTEGER DEFAULT 0,  -- 价格（分）
    language VARCHAR(10) DEFAULT 'zh-CN',  -- 语言
    rating REAL DEFAULT 0.0,  -- 评分（0-5）
    rating_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,  -- 浏览次数
    download_count INTEGER DEFAULT 0,  -- 下载次数
    status TINYINT DEFAULT 1,  -- 1-上架 0-下架
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id)
);

-- 索引
CREATE INDEX idx_ebook_category ON tb_ebook(category_id);
CREATE INDEX idx_ebook_language ON tb_ebook(language);
CREATE INDEX idx_ebook_author ON tb_ebook(author);
CREATE INDEX idx_ebook_status ON tb_ebook(status);
```

**字段说明**:
- `book_id`: 书籍ID
- `book_title`: 书名
- `author`: 作者
- `isbn`: ISBN编号
- `publisher`: 出版社
- `publish_date`: 出版日期
- `book_intro`: 简介（TEXT类型，支持长文本）
- `category_id`: 分类ID
- `cover_img`: 封面图片路径
- `file_format`: 文件格式（PDF/EPUB/MOBI）
- `file_path`: 文件路径
- `file_size`: 文件大小（字节）
- `page_count`: 页数
- `price`: 价格（分）
- `language`: 语言代码（zh-CN/en-US等）
- `rating`: 平均评分（0-5）
- `rating_count`: 评分人数
- `view_count`: 浏览次数（替代用户行为追踪）
- `download_count`: 下载次数（替代下载记录表）
- `status`: 上架状态（1-上架 0-下架）
- `create_time`: 创建时间
- `update_time`: 更新时间

**简化说明**:
- 合并了原价和售价为单一 `price` 字段
- 删除了 `is_deleted` 字段（直接物理删除）
- 删除了 `create_user` 和 `update_user` 字段
- 删除了 `detail_content` 字段（合并到 book_intro）
- 新增 `view_count` 和 `download_count` 替代行为追踪表
- 标签使用逗号分隔的字符串，不使用关联表

**对比 newbee-mall**:
- 新增：isbn, publisher, file_format, file_path, file_size, language, view_count, download_count
- 删除：stock_num, goods_carousel, is_deleted, create_user, update_user

#### 4. tb_order (订单表)

```sql
CREATE TABLE IF NOT EXISTS tb_order (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no VARCHAR(20) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL,
    total_price INTEGER NOT NULL,
    pay_status TINYINT DEFAULT 0,  -- 0-未支付 1-已支付
    pay_time DATETIME,
    order_status TINYINT DEFAULT 0,  -- 0-待支付 1-已完成 2-已取消
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

CREATE INDEX idx_order_user ON tb_order(user_id);
CREATE INDEX idx_order_status ON tb_order(order_status);
```

**简化说明**:
- 删除了支付方式字段（简化支付流程）
- 删除了额外信息字段
- 订单状态简化为 3 个（待支付、已完成、已取消）

#### 5. tb_order_item (订单项表)

```sql
CREATE TABLE IF NOT EXISTS tb_order_item (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    book_title VARCHAR(200),
    price INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES tb_order(order_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id)
);
```

**简化说明**:
- 保留核心字段
- 删除了封面图片字段（可从电子书表关联获取）

#### 6. tb_cart (购物车表)

```sql
CREATE TABLE IF NOT EXISTS tb_cart (
    cart_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)  -- 防止重复添加
);

CREATE INDEX idx_cart_user ON tb_cart(user_id);
```

**简化说明**:
- 删除了数量字段（电子书无需数量）
- 删除了删除标识字段
- 添加唯一约束防止重复

#### 7. tb_favorite (收藏表)

```sql
CREATE TABLE IF NOT EXISTS tb_favorite (
    favorite_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)
);

CREATE INDEX idx_favorite_user ON tb_favorite(user_id);
```

**简化说明**:
- 保留核心收藏功能
- 添加唯一约束防止重复收藏

#### 8. tb_review (评价表)

```sql
CREATE TABLE IF NOT EXISTS tb_review (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    rating TINYINT NOT NULL,  -- 1-5星
    content TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id)
);

CREATE INDEX idx_review_book ON tb_review(book_id);
```

**简化说明**:
- 删除了点赞数字段（简化功能）
- 删除了删除标识字段
- 删除了更新时间字段
- 删除了评价点赞关联表

#### 9. tb_user_interest (用户兴趣表) - 新增

```sql
CREATE TABLE IF NOT EXISTS tb_user_interest (
    interest_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id),
    UNIQUE(user_id, category_id)
);

CREATE INDEX idx_user_interest_user ON tb_user_interest(user_id);
CREATE INDEX idx_user_interest_category ON tb_user_interest(category_id);
```

**功能说明**:
- 用户主动选择感兴趣的分类
- 用于推荐算法的冷启动问题
- 支持基于兴趣的个性化推荐
- 可根据用户行为自动更新兴趣标签

**使用场景**:
- 新用户注册时选择兴趣标签
- 用户在个人中心管理兴趣偏好
- 推荐系统根据兴趣标签推荐相关书籍
- 解决新用户无历史行为的冷启动问题

---

### 删除的表（简化内容）

以下表在简化版本中被删除或合并：

| 原表名 | 处理方式 | 说明 |
|--------|----------|------|
| tb_mall_user_token | ❌ 删除 | 使用 Session 管理 |
| tb_admin_user | ✅ 合并 | 合并到 tb_user（is_admin 字段） |
| tb_admin_user_token | ❌ 删除 | 使用 Session 管理 |
| tb_ebook_tag | ❌ 删除 | 使用字符串字段替代 |
| tb_ebook_tag_relation | ❌ 删除 | 使用逗号分隔的 tags 字段 |
| tb_user_behavior | ❌ 删除 | 使用 view_count 替代 |
| tb_reading_progress | ❌ 删除 | 可选功能 |
| tb_download_record | ❌ 删除 | 使用 download_count 替代 |
| tb_review_like | ❌ 删除 | 简化评价功能 |
| tb_carousel | ❌ 删除 | 可选功能 |
| tb_index_config | ❌ 删除 | 可选功能 |
| tb_recommendation_cache | ❌ 删除 | 简化推荐功能 |

---

### 简化效果总结

| 指标 | 原设计 | 当前方案 | 改善 |
|------|--------|----------|------|
| 表数量 | 15+ 张 | 12 张 | ⬇️ 20% |
| 平均字段数 | 20+ 字段/表 | 10-12 字段/表 | ⬇️ 35% |
| 索引数量 | 30+ 个 | 18 个 | ⬇️ 40% |
| 外键约束 | 20+ 个 | 12 个 | ⬇️ 40% |
| 代码复杂度 | 高 | 中 | ⬇️ 35% |

**核心功能保留率**: 90%+（含创新功能）  
**开发效率提升**: 40%+  
**适用场景**: 毕业设计、原型开发、演示系统  
**创新点**: 
- 书单功能（UGC用户生成内容）
- 用户兴趣标签（解决冷启动）
- 推荐算法（协同过滤+兴趣推荐）

#### 2. tb_ebook_category (电子书分类表) - 已删除，见上方 tb_category

```sql
CREATE TABLE `tb_ebook_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_level` tinyint(4) NOT NULL COMMENT '分类级别(1-一级 2-二级)',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_name_en` varchar(50) DEFAULT '' COMMENT '分类英文名称',
  `category_rank` int(11) NOT NULL DEFAULT 0 COMMENT '排序值',
  `category_icon` varchar(200) DEFAULT '' COMMENT '分类图标',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` int(11) NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` int(11) NOT NULL DEFAULT 0 COMMENT '更新者ID',
  PRIMARY KEY (`category_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`category_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子书分类表';
```

**对比 newbee-mall 的分类表**:
- 新增字段：category_name_en (支持多语言显示)
- 保留字段：基本的分类层级结构
- 支持特性：二级分类、排序、多语言名称

**多语言分类支持**:
- category_name: 中文分类名称
- category_name_en: 英文分类名称
- 前端根据用户语言偏好显示对应名称
- 支持未来扩展更多语言字段（如 category_name_ja、category_name_ko 等）

**推荐的分类体系**:

**一级分类（中英文对照）**:
1. 计算机与互联网 / Computer & Internet
2. 文学小说 / Literature & Fiction  
3. 经济管理 / Business & Economics
4. 教育学习 / Education & Learning
5. 生活健康 / Health & Lifestyle
6. 历史人文 / History & Humanities
7. 科学技术 / Science & Technology
8. 艺术设计 / Arts & Design

**二级分类示例**:
- 计算机与互联网：Java, Python, JavaScript, 前端开发/Frontend Development, 后端开发/Backend Development, 算法与数据结构/Algorithms & Data Structures
- 文学小说：科幻小说/Science Fiction, 悬疑推理/Mystery & Thriller, 都市言情/Romance, 文学经典/Literary Classics
- 经济管理：企业管理/Business Management, 市场营销/Marketing, 金融投资/Finance & Investment

#### 3. tb_user_behavior (用户行为表) - 新增

```sql
CREATE TABLE `tb_user_behavior` (
  `behavior_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '行为ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '书籍ID',
  `behavior_type` varchar(20) NOT NULL COMMENT '行为类型(VIEW/SEARCH/PURCHASE/FAVORITE)',
  `behavior_data` text COMMENT '行为数据(JSON格式)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`behavior_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_book_id` (`book_id`),
  KEY `idx_behavior_type` (`behavior_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为表';
```

#### 3. tb_reading_progress (阅读进度表) - 新增

```sql
CREATE TABLE `tb_reading_progress` (
  `progress_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '进度ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '书籍ID',
  `current_page` int(11) DEFAULT 0 COMMENT '当前页码',
  `progress_percent` decimal(5,2) DEFAULT 0.00 COMMENT '进度百分比',
  `last_position` text COMMENT '最后位置(JSON格式)',
  `last_read_time` datetime NOT NULL COMMENT '最后阅读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`progress_id`),
  UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
  KEY `idx_last_read_time` (`last_read_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阅读进度表';
```

#### 4. tb_ebook_review (电子书评价表) - 新增

```sql
CREATE TABLE `tb_ebook_review` (
  `review_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '书籍ID',
  `rating` tinyint(4) NOT NULL COMMENT '评分(1-5)',
  `review_content` text COMMENT '评价内容',
  `like_count` int(11) DEFAULT 0 COMMENT '点赞数',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`review_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_book_id` (`book_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子书评价表';
```

#### 5. tb_ebook_order (电子书订单表)

```sql
CREATE TABLE `tb_ebook_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(20) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_price` int(11) NOT NULL COMMENT '总价(分)',
  `pay_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '支付状态(0-未支付 1-已支付)',
  `pay_type` tinyint(4) DEFAULT 0 COMMENT '支付方式(1-支付宝 2-微信 3-余额)',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `order_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订单状态(0-待支付 1-已支付 2-已取消)',
  `extra_info` varchar(500) DEFAULT '' COMMENT '额外信息',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子书订单表';
```

**对比 newbee-mall 的 tb_newbee_mall_order**:
- 保持一致：基本字段结构相同
- 说明：由于电子书无需物流，不需要关联地址表


## API 接口设计

### RESTful API 规范

所有 API 遵循 RESTful 设计原则，统一响应格式：

```json
{
  "resultCode": 200,
  "message": "success",
  "data": {}
}
```

### 核心 API 端点

#### 1. 用户相关 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| POST | /api/v1/user/register | 用户注册 | 保留 |
| POST | /api/v1/user/login | 用户登录 | 保留 |
| POST | /api/v1/user/logout | 用户登出 | 保留 |
| GET | /api/v1/user/info | 获取用户信息 | 保留 |
| PUT | /api/v1/user/info | 更新用户信息 | 保留 |
| POST | /api/v1/user/email/verify | 邮箱验证 | 新增 |

#### 2. 电子书相关 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| GET | /api/v1/ebooks | 获取电子书列表 | 改造自商品列表 |
| GET | /api/v1/ebooks/{id} | 获取电子书详情 | 改造自商品详情 |
| GET | /api/v1/ebooks/search | 搜索电子书 | 增强搜索功能 |
| GET | /api/v1/ebooks/advanced-search | 高级搜索 | 新增 |
| GET | /api/v1/ebooks/{id}/tags | 获取电子书标签 | 新增 |
| GET | /api/v1/ebooks/by-author | 按作者查询 | 新增 |
| GET | /api/v1/ebooks/by-isbn | 按ISBN查询 | 新增 |

**多语言支持参数**:
- `language`: 语言代码参数，支持 zh-CN(中文)、en-US(英语)、ja-JP(日语)、ko-KR(韩语)等
- `crossLanguage`: 跨语言搜索开关，默认 false

**API 示例**:
```
GET /api/v1/ebooks?language=zh-CN&categoryId=1&pageNumber=1&pageSize=10
GET /api/v1/ebooks/search?keyword=Java&language=en-US&crossLanguage=false
GET /api/v1/ebooks/advanced-search?author=Martin&language=en-US&categoryId=101
```

#### 3. 分类相关 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| GET | /api/v1/categories | 获取分类列表 | 保留 |
| GET | /api/v1/categories/{id}/ebooks | 获取分类下的电子书 | 保留 |

**多语言分类支持**:
- 分类名称根据 `language` 参数返回对应语言版本
- 默认返回中文名称，传入 `language=en-US` 返回英文名称
- 支持分类树的多语言展示

**API 示例**:
```
GET /api/v1/categories?language=zh-CN  // 返回中文分类名称
GET /api/v1/categories?language=en-US  // 返回英文分类名称
GET /api/v1/categories/1/ebooks?language=zh-CN&pageNumber=1&pageSize=10
```

#### 4. 购物车相关 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| GET | /api/v1/cart | 获取购物车 | 保留 |
| POST | /api/v1/cart | 添加到购物车 | 保留 |
| DELETE | /api/v1/cart/{id} | 删除购物车项 | 保留 |
| GET | /api/v1/cart/check-purchased/{bookId} | 检查是否已购买 | 新增 |

#### 5. 订单相关 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| POST | /api/v1/orders | 创建订单 | 保留 |
| GET | /api/v1/orders | 获取订单列表 | 保留 |
| GET | /api/v1/orders/{orderNo} | 获取订单详情 | 保留 |
| PUT | /api/v1/orders/{orderNo}/cancel | 取消订单 | 保留 |
| PUT | /api/v1/orders/{orderNo}/pay | 支付订单 | 保留 |
| GET | /api/v1/orders/purchased-ebooks | 获取已购电子书 | 新增 |

#### 6. 阅读相关 API - 新增

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/reading/{bookId}/content | 获取电子书内容 |
| GET | /api/v1/reading/{bookId}/progress | 获取阅读进度 |
| POST | /api/v1/reading/{bookId}/progress | 保存阅读进度 |
| GET | /api/v1/reading/recent | 获取最近阅读 |
| POST | /api/v1/reading/{bookId}/download | 生成下载链接 |
| GET | /api/v1/reading/{bookId}/download-record | 获取下载记录 |

#### 7. 推荐相关 API - 新增

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/recommendations/personal | 个性化推荐 |
| GET | /api/v1/recommendations/hot | 热门推荐 |
| GET | /api/v1/recommendations/similar/{bookId} | 相似推荐 |
| GET | /api/v1/recommendations/category/{categoryId} | 分类推荐 |
| POST | /api/v1/recommendations/track | 追踪用户行为 |

#### 8. 评价相关 API - 新增

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/reviews/{bookId} | 获取电子书评价 |
| POST | /api/v1/reviews | 提交评价 |
| PUT | /api/v1/reviews/{id} | 更新评价 |
| DELETE | /api/v1/reviews/{id} | 删除评价 |
| POST | /api/v1/reviews/{id}/like | 点赞评价 |
| DELETE | /api/v1/reviews/{id}/like | 取消点赞 |

#### 9. 用户兴趣相关 API - 新增

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/user/interests | 获取用户兴趣列表 |
| POST | /api/v1/user/interests | 添加兴趣（单个分类） |
| POST | /api/v1/user/interests/batch | 批量添加兴趣 |
| DELETE | /api/v1/user/interests/{categoryId} | 删除兴趣 |
| GET | /api/v1/user/interests/check/{categoryId} | 检查是否已选择该兴趣 |
| GET | /api/v1/user/interests/recommendations | 基于兴趣的推荐 |

#### 10. 收藏相关 API - 新增

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/favorites | 获取收藏列表 |
| POST | /api/v1/favorites/{bookId} | 添加收藏 |
| DELETE | /api/v1/favorites/{bookId} | 取消收藏 |
| GET | /api/v1/favorites/check/{bookId} | 检查是否收藏 |

#### 11. 管理后台 API

| 方法 | 路径 | 说明 | 对比 newbee-mall |
|------|------|------|------------------|
| POST | /api/v1/admin/login | 管理员登录 | 保留 |
| GET | /api/v1/admin/ebooks | 获取电子书列表 | 改造自商品管理 |
| POST | /api/v1/admin/ebooks | 添加电子书 | 改造自商品管理 |
| PUT | /api/v1/admin/ebooks/{id} | 更新电子书 | 改造自商品管理 |
| DELETE | /api/v1/admin/ebooks/{id} | 删除电子书 | 改造自商品管理 |
| POST | /api/v1/admin/ebooks/upload | 上传电子书文件 | 新增 |
| POST | /api/v1/admin/ebooks/batch-import | 批量导入 | 新增 |
| GET | /api/v1/admin/orders | 获取订单列表 | 保留 |
| GET | /api/v1/admin/users | 获取用户列表 | 保留 |
| GET | /api/v1/admin/statistics | 获取统计数据 | 新增 |


## 多语言支持设计

### 概述

IntelliBook-Mall 支持多语言电子书分类和检索，主要面向中文和英文用户，同时预留了扩展其他语言的能力。

### 支持的语言

#### 主要语言
- **zh-CN**: 简体中文（默认）
- **en-US**: 英语
- **ja-JP**: 日语（预留）
- **ko-KR**: 韩语（预留）

#### 语言代码标准
使用 ISO 639-1 + ISO 3166-1 标准：
- 语言代码：zh（中文）、en（英语）、ja（日语）、ko（韩语）
- 地区代码：CN（中国）、US（美国）、JP（日本）、KR（韩国）

### 数据库设计

#### 电子书语言字段
```sql
-- tb_ebook 表中的语言字段
`language` varchar(10) NOT NULL DEFAULT 'zh-CN' COMMENT '语言(zh-CN中文, en-US英文等)'

-- 相关索引
KEY `idx_language` (`language`),
KEY `idx_language_category` (`language`, `category_id`)
```

#### 分类多语言字段
```sql
-- tb_ebook_category 表中的多语言字段
`category_name` varchar(50) NOT NULL COMMENT '分类名称（中文）',
`category_name_en` varchar(50) DEFAULT '' COMMENT '分类英文名称'

-- 未来可扩展
-- `category_name_ja` varchar(50) DEFAULT '' COMMENT '分类日文名称'
-- `category_name_ko` varchar(50) DEFAULT '' COMMENT '分类韩文名称'
```

### API 接口设计

#### 语言参数规范
所有支持多语言的 API 接口都支持以下参数：

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| language | String | zh-CN | 语言代码，影响返回的分类名称和筛选结果 |
| crossLanguage | Boolean | false | 是否跨语言搜索（仅搜索接口支持） |

#### 接口示例

**获取分类列表**:
```http
GET /api/v1/categories?language=zh-CN
GET /api/v1/categories?language=en-US
```

**获取电子书列表**:
```http
GET /api/v1/ebooks?language=zh-CN&categoryId=1
GET /api/v1/ebooks?language=en-US&categoryId=1
```

**搜索电子书**:
```http
# 仅搜索中文书籍
GET /api/v1/ebooks/search?keyword=Java&language=zh-CN&crossLanguage=false

# 搜索所有语言书籍
GET /api/v1/ebooks/search?keyword=Java&crossLanguage=true
```

### 前端实现

#### 语言切换器
```html
<div class="language-switcher">
  <button class="lang-btn active" data-lang="zh-CN">🇨🇳 中文</button>
  <button class="lang-btn" data-lang="en-US">🇺🇸 English</button>
</div>
```

#### 分类导航多语言显示
```javascript
// 根据当前语言显示分类名称
const getCategoryName = (category, currentLang) => {
  return currentLang === 'en-US' ? category.categoryNameEn : category.categoryName;
};
```

#### 语言偏好存储
```javascript
// 保存用户语言偏好
localStorage.setItem('preferred-language', selectedLang);

// 自动检测浏览器语言
const browserLang = navigator.language || navigator.userLanguage;
const defaultLang = browserLang.startsWith('zh') ? 'zh-CN' : 'en-US';
```

### 搜索功能增强

#### 语言筛选搜索
- **单语言搜索**: 仅搜索指定语言的电子书
- **跨语言搜索**: 搜索所有语言的电子书，按语言分组显示结果

#### 搜索结果展示
```json
{
  "resultCode": 200,
  "message": "success",
  "data": {
    "totalCount": 25,
    "languages": {
      "zh-CN": {
        "count": 15,
        "books": [...]
      },
      "en-US": {
        "count": 10,
        "books": [...]
      }
    }
  }
}
```

### 推荐算法多语言支持

#### 语言偏好推荐
- 优先推荐用户偏好语言的书籍
- 同类型跨语言推荐作为补充
- 考虑用户语言能力进行智能推荐

#### 推荐策略
```java
public List<EBook> getLanguageAwareRecommendations(Long userId, String preferredLang) {
    // 70% 推荐偏好语言书籍
    List<EBook> preferredBooks = getRecommendationsByLanguage(userId, preferredLang, 7);
    
    // 30% 推荐其他语言书籍
    List<EBook> otherBooks = getRecommendationsByOtherLanguages(userId, preferredLang, 3);
    
    return mergeRecommendations(preferredBooks, otherBooks);
}
```

### 内容管理策略

#### 中文书籍重点
- 技术类：Java、Python、前端开发、算法等
- 商业类：企业管理、市场营销、创业指南等
- 文学类：现代文学、科幻小说、经典名著等

#### 英文书籍重点
- 技术类：经典原版技术书籍、最新技术趋势
- 商业类：国际商业管理、投资理财经典
- 学术类：计算机科学、数据科学等学术著作

#### 定价策略
- 中文书籍：适合国内消费水平，价格相对较低
- 英文原版：体现原版价值，价格适当提高
- 翻译版本：介于中英文书籍之间

### 扩展性设计

#### 数据库扩展
```sql
-- 未来添加新语言只需增加字段
ALTER TABLE tb_ebook_category ADD COLUMN `category_name_ja` varchar(50) DEFAULT '' COMMENT '分类日文名称';
ALTER TABLE tb_ebook_category ADD COLUMN `category_name_ko` varchar(50) DEFAULT '' COMMENT '分类韩文名称';
```

#### 配置文件扩展
```properties
# 支持的语言列表
ebook.supported-languages=zh-CN,en-US,ja-JP,ko-KR

# 默认语言
ebook.default-language=zh-CN

# 语言检测开关
ebook.auto-detect-language=true
```

## 错误处理

### 统一异常处理

继承 newbee-mall 的异常处理机制，使用 `@ControllerAdvice` 统一处理异常：

```java
@ControllerAdvice
public class IntelliBookExceptionHandler {
    
    @ExceptionHandler(IntelliBookException.class)
    public Result handleIntelliBookException(IntelliBookException e) {
        return ResultGenerator.genFailResult(e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return ResultGenerator.genFailResult("系统异常");
    }
}
```

### 业务异常枚举

```java
public enum ServiceResultEnum {
    // 保留自 newbee-mall
    SUCCESS("success"),
    ERROR("error"),
    LOGIN_ERROR("登录失败"),
    USER_NOT_EXIST("用户不存在"),
    
    // 新增电子书相关
    EBOOK_NOT_EXIST("电子书不存在"),
    EBOOK_FILE_NOT_FOUND("电子书文件未找到"),
    EBOOK_ALREADY_PURCHASED("电子书已购买"),
    EBOOK_NOT_PURCHASED("电子书未购买"),
    
    // 新增阅读相关
    NO_READING_PERMISSION("无阅读权限"),
    NO_DOWNLOAD_PERMISSION("无下载权限"),
    DOWNLOAD_LIMIT_EXCEEDED("下载次数已达上限"),
    
    // 新增评价相关
    REVIEW_PERMISSION_DENIED("无评价权限，请先购买"),
    REVIEW_ALREADY_EXISTS("已评价过该电子书"),
    
    // 新增推荐相关
    RECOMMENDATION_NOT_AVAILABLE("推荐暂不可用");
    
    private String result;
    
    ServiceResultEnum(String result) {
        this.result = result;
    }
    
    public String getResult() {
        return result;
    }
}
```

## 测试策略

### 单元测试

使用 JUnit 5 和 Mockito 进行单元测试，覆盖核心业务逻辑：

**测试范围**:
- Service 层业务逻辑测试
- Mapper 层数据访问测试
- 工具类方法测试

**测试示例**:
```java
@SpringBootTest
class EBookServiceTest {
    
    @Autowired
    private EBookService eBookService;
    
    @Test
    void testGetEBookById() {
        EBook ebook = eBookService.getEBookById(1L);
        assertNotNull(ebook);
        assertEquals("测试书名", ebook.getBookTitle());
    }
    
    @Test
    void testSearchEBooks() {
        EBookSearchParam param = new EBookSearchParam();
        param.setKeyword("Java");
        PageResult<EBook> result = eBookService.searchEBooks(param);
        assertTrue(result.getTotalCount() > 0);
    }
}
```

### 集成测试

使用 Spring Boot Test 进行集成测试：

**测试范围**:
- API 接口测试
- 数据库事务测试
- 文件上传下载测试

### 性能测试

使用 JMeter 进行性能测试：

**测试指标**:
- 并发用户数：1000
- 响应时间：< 3s (首页加载)
- 吞吐量：> 100 TPS
- 错误率：< 1%

**测试场景**:
1. 首页加载测试
2. 搜索功能压力测试
3. 订单创建并发测试
4. 文件下载并发测试


## 安全设计

### 身份认证

**Token 认证机制** (继承自 newbee-mall):
- 用户登录成功后生成 Token
- Token 存储在数据库表 `tb_mall_user_token`
- 每次请求携带 Token 进行身份验证
- Token 有效期：7 天

**增强安全措施**:
- 密码使用 MD5 + 盐值加密
- 登录失败 5 次锁定账户 30 分钟
- Token 定期刷新机制

### 权限控制

使用自定义注解进行权限校验：

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenToUser {
}

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenToAdminUser {
}
```

### 数据安全

**电子书文件保护**:
- 文件存储路径加密
- 下载链接临时生成，24 小时有效
- 下载次数限制
- 防盗链机制

**敏感数据加密**:
- 用户密码 MD5 加密
- 支付信息加密传输
- 个人信息脱敏展示

### SQL 注入防护

使用 MyBatis 参数化查询防止 SQL 注入：

```xml
<select id="getEBookById" resultType="EBook">
    SELECT * FROM tb_ebook WHERE book_id = #{bookId}
</select>
```

### XSS 防护

对用户输入进行 HTML 转义：
- 评论内容过滤
- 搜索关键词过滤
- 用户昵称过滤

## 性能优化

### 数据库优化

**索引设计**:
- 主键索引：所有表的主键
- 唯一索引：ISBN、订单号
- 普通索引：作者、分类、标签、创建时间
- 复合索引：(user_id, book_id) 用于阅读进度和收藏

**查询优化**:
- 分页查询使用 LIMIT
- 避免 SELECT *，只查询需要的字段
- 使用连接查询代替子查询
- 定期分析慢查询日志

### 缓存策略 (可选 Redis)

**缓存内容**:
- 热门电子书列表 (TTL: 1小时)
- 分类树结构 (TTL: 24小时)
- 用户推荐结果 (TTL: 30分钟)
- 电子书详情 (TTL: 1小时)

**缓存更新策略**:
- 主动更新：数据修改时清除缓存
- 被动更新：缓存过期后重新加载
- 定时更新：定时任务刷新热点数据

### 文件存储优化

**文件分片存储**:
- 大文件分片上传
- 断点续传支持
- CDN 加速（可选）

**文件压缩**:
- 封面图片压缩
- 缩略图生成

### 前端优化

**资源优化**:
- 静态资源 CDN 加速
- 图片懒加载
- 代码分割和按需加载
- Gzip 压缩

**接口优化**:
- 接口合并减少请求次数
- 数据分页加载
- 防抖和节流

## 部署架构

### 开发环境

```
开发机 → Spring Boot (内置 Tomcat) → MySQL (本地)
```

### 生产环境

```
用户 → Nginx (负载均衡) → Spring Boot 集群 → MySQL 主从 + Redis
                                          ↓
                                    文件存储服务器
```

**服务器配置建议**:
- 应用服务器：2核4G，2台
- 数据库服务器：4核8G，主从架构
- 文件服务器：4核8G，大容量存储
- Redis 服务器：2核4G

### 监控和日志

**应用监控**:
- Spring Boot Actuator 健康检查
- 接口响应时间监控
- 异常告警

**日志管理**:
- 使用 Logback 记录日志
- 日志分级：DEBUG、INFO、WARN、ERROR
- 日志文件按日期滚动
- 重要操作审计日志


## 推荐算法设计

### 推荐策略

IntelliBook-Mall 采用混合推荐策略，结合多种推荐算法：

#### 1. 协同过滤推荐 (Collaborative Filtering)

**基于用户的协同过滤**:
- 计算用户之间的相似度（基于购买历史）
- 推荐相似用户喜欢的电子书

**基于物品的协同过滤**:
- 计算电子书之间的相似度（基于用户购买行为）
- 推荐用户已购买书籍的相似书籍

**相似度计算**:
```java
// 余弦相似度
public double cosineSimilarity(Set<Long> set1, Set<Long> set2) {
    Set<Long> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);
    
    if (set1.isEmpty() || set2.isEmpty()) {
        return 0.0;
    }
    
    return intersection.size() / 
           (Math.sqrt(set1.size()) * Math.sqrt(set2.size()));
}
```

#### 2. 基于内容的推荐 (Content-Based)

**特征提取**:
- 分类相似度
- 标签匹配度
- 作者相同
- 出版社相同

**推荐逻辑**:
```java
public List<EBook> getContentBasedRecommendations(Long bookId) {
    EBook sourceBook = eBookMapper.selectByPrimaryKey(bookId);
    
    // 构建查询条件
    EBookSearchParam param = new EBookSearchParam();
    param.setCategoryId(sourceBook.getCategoryId());
    param.setTagIds(parseTagIds(sourceBook.getTags()));
    param.setAuthor(sourceBook.getAuthor());
    
    // 查询相似书籍
    List<EBook> candidates = eBookMapper.searchEBooks(param);
    
    // 计算相似度并排序
    return candidates.stream()
        .map(book -> new ScoredBook(book, calculateSimilarity(sourceBook, book)))
        .sorted(Comparator.comparing(ScoredBook::getScore).reversed())
        .limit(10)
        .map(ScoredBook::getBook)
        .collect(Collectors.toList());
}
```

#### 3. 热度推荐 (Popularity-Based)

**热度计算公式**:
```
热度分数 = 购买次数 * 0.5 + 收藏次数 * 0.2 + 浏览次数 * 0.1 + 评分 * 0.2
```

**时间衰减**:
```java
public double calculateHotScore(EBook book, List<UserBehavior> behaviors) {
    double score = 0.0;
    long now = System.currentTimeMillis();
    
    for (UserBehavior behavior : behaviors) {
        long timeDiff = now - behavior.getCreateTime().getTime();
        double decay = Math.exp(-timeDiff / (7 * 24 * 3600 * 1000.0)); // 7天衰减
        
        switch (behavior.getBehaviorType()) {
            case "PURCHASE":
                score += 0.5 * decay;
                break;
            case "FAVORITE":
                score += 0.2 * decay;
                break;
            case "VIEW":
                score += 0.1 * decay;
                break;
        }
    }
    
    score += book.getAvgRating() * 0.2;
    return score;
}
```

#### 4. 混合推荐策略

**推荐结果融合**:
```java
public List<EBook> getHybridRecommendations(Long userId, Integer limit) {
    // 获取各种推荐结果
    List<EBook> cfBooks = getCollaborativeFilteringBooks(userId, limit * 2);
    List<EBook> contentBooks = getContentBasedBooks(userId, limit * 2);
    List<EBook> hotBooks = getHotBooks(limit);
    
    // 权重融合
    Map<Long, Double> scoreMap = new HashMap<>();
    
    // 协同过滤权重 0.4
    cfBooks.forEach(book -> 
        scoreMap.merge(book.getBookId(), 0.4, Double::sum));
    
    // 内容推荐权重 0.3
    contentBooks.forEach(book -> 
        scoreMap.merge(book.getBookId(), 0.3, Double::sum));
    
    // 热度推荐权重 0.3
    hotBooks.forEach(book -> 
        scoreMap.merge(book.getBookId(), 0.3, Double::sum));
    
    // 排序并返回
    return scoreMap.entrySet().stream()
        .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
        .limit(limit)
        .map(entry -> eBookMapper.selectByPrimaryKey(entry.getKey()))
        .collect(Collectors.toList());
}
```

### 冷启动问题处理

**新用户**:
- 注册时引导用户选择感兴趣的分类（3-5个）
- 基于用户选择的兴趣标签推荐相关书籍
- 展示热门电子书作为补充
- 展示编辑推荐
- 随着用户行为积累，逐步过渡到个性化推荐

**新电子书**:
- 基于分类推荐给选择了该分类兴趣的用户
- 基于标签推荐给相关用户
- 在首页新品区展示

**基于兴趣的推荐算法**:
```java
public List<EBook> getInterestBasedRecommendations(Long userId, Integer limit) {
    // 获取用户兴趣分类
    List<Long> interestCategoryIds = userInterestMapper.getUserInterestCategories(userId);
    
    if (interestCategoryIds.isEmpty()) {
        // 如果用户未设置兴趣，返回热门书籍
        return getHotBooks(limit);
    }
    
    // 从用户感兴趣的分类中获取高评分书籍
    List<EBook> recommendations = new ArrayList<>();
    for (Long categoryId : interestCategoryIds) {
        List<EBook> categoryBooks = eBookMapper.getTopRatedBooksByCategory(
            categoryId, limit / interestCategoryIds.size() + 1);
        recommendations.addAll(categoryBooks);
    }
    
    // 按评分和浏览量排序
    return recommendations.stream()
        .sorted(Comparator.comparing(EBook::getRating)
            .thenComparing(EBook::getViewCount).reversed())
        .limit(limit)
        .collect(Collectors.toList());
}
```

## 文件管理设计

### 文件存储结构

```
/ebook-files/
  ├── covers/              # 封面图片
  │   ├── 2024/
  │   │   ├── 01/
  │   │   │   ├── {bookId}_cover.jpg
  ├── books/               # 电子书文件
  │   ├── pdf/
  │   │   ├── {bookId}.pdf
  │   ├── epub/
  │   │   ├── {bookId}.epub
  │   ├── mobi/
  │   │   ├── {bookId}.mobi
  ├── temp/                # 临时文件
      ├── uploads/         # 上传临时目录
```

### 文件上传流程

```mermaid
sequenceDiagram
    participant Admin as 管理员
    participant API as API服务
    participant Storage as 文件存储
    participant DB as 数据库
    
    Admin->>API: 上传电子书文件
    API->>API: 验证文件格式和大小
    API->>Storage: 保存文件到临时目录
    API->>API: 提取元数据(标题、作者等)
    API->>DB: 创建电子书记录
    API->>Storage: 移动文件到正式目录
    API->>DB: 更新文件路径
    API-->>Admin: 返回上传结果
```

### 文件下载流程

```mermaid
sequenceDiagram
    participant User as 用户
    participant API as API服务
    participant DB as 数据库
    participant Storage as 文件存储
    
    User->>API: 请求下载电子书
    API->>DB: 验证购买记录
    API->>DB: 检查下载次数限制
    API->>API: 生成临时下载链接
    API->>DB: 记录下载记录
    API-->>User: 返回下载链接
    User->>Storage: 通过链接下载文件
    Storage-->>User: 返回文件流
```

### 文件安全

**访问控制**:
- 文件路径不直接暴露给前端
- 通过 API 接口验证权限后返回临时链接
- 临时链接包含签名和过期时间

**防盗链**:
```java
public String generateSecureDownloadUrl(Long userId, Long bookId) {
    // 生成签名
    String timestamp = String.valueOf(System.currentTimeMillis());
    String signature = MD5Util.MD5Encode(
        userId + bookId + timestamp + SECRET_KEY, "UTF-8");
    
    // 构建下载链接
    return String.format("/api/v1/download?bookId=%d&userId=%d&timestamp=%s&sign=%s",
        bookId, userId, timestamp, signature);
}

public boolean verifyDownloadUrl(Long userId, Long bookId, 
                                  String timestamp, String signature) {
    // 验证时间戳（24小时有效）
    long now = System.currentTimeMillis();
    long requestTime = Long.parseLong(timestamp);
    if (now - requestTime > 24 * 3600 * 1000) {
        return false;
    }
    
    // 验证签名
    String expectedSign = MD5Util.MD5Encode(
        userId + bookId + timestamp + SECRET_KEY, "UTF-8");
    return signature.equals(expectedSign);
}
```


## 在线阅读器设计

### 技术选型

**PDF 阅读器**: PDF.js
- Mozilla 开源项目
- 纯 JavaScript 实现
- 支持文本选择、搜索、缩放

**EPUB 阅读器**: Epub.js
- 开源 EPUB 阅读器库
- 支持响应式布局
- 支持主题切换

### 阅读器功能

**基础功能**:
- 翻页（上一页/下一页）
- 跳转到指定页
- 目录导航
- 书签管理
- 全屏模式

**高级功能**:
- 文本搜索
- 字体大小调整
- 背景颜色切换
- 阅读进度显示
- 自动保存阅读位置

### 前端集成示例

```javascript
// PDF.js 集成
import * as pdfjsLib from 'pdfjs-dist';

class PDFReader {
  constructor(container, pdfUrl) {
    this.container = container;
    this.pdfUrl = pdfUrl;
    this.pdfDoc = null;
    this.currentPage = 1;
  }
  
  async init() {
    const loadingTask = pdfjsLib.getDocument(this.pdfUrl);
    this.pdfDoc = await loadingTask.promise;
    await this.renderPage(this.currentPage);
  }
  
  async renderPage(pageNum) {
    const page = await this.pdfDoc.getPage(pageNum);
    const viewport = page.getViewport({ scale: 1.5 });
    
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    canvas.height = viewport.height;
    canvas.width = viewport.width;
    
    await page.render({
      canvasContext: context,
      viewport: viewport
    }).promise;
    
    this.container.innerHTML = '';
    this.container.appendChild(canvas);
    
    // 保存阅读进度
    this.saveProgress(pageNum);
  }
  
  async saveProgress(pageNum) {
    await fetch('/api/v1/reading/progress', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        bookId: this.bookId,
        currentPage: pageNum,
        progressPercent: (pageNum / this.pdfDoc.numPages) * 100
      })
    });
  }
  
  nextPage() {
    if (this.currentPage < this.pdfDoc.numPages) {
      this.currentPage++;
      this.renderPage(this.currentPage);
    }
  }
  
  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.renderPage(this.currentPage);
    }
  }
}
```

### 阅读进度同步

**自动保存机制**:
- 每 30 秒自动保存一次
- 翻页时立即保存
- 关闭页面前保存

**进度恢复**:
```java
@GetMapping("/reading/{bookId}/resume")
public Result resumeReading(@PathVariable Long bookId, 
                            @TokenToUser MallUser user) {
    ReadingProgress progress = readingService.getReadingProgress(
        user.getUserId(), bookId);
    
    if (progress == null) {
        return ResultGenerator.genSuccessResult(
            Map.of("currentPage", 1, "progressPercent", 0.0));
    }
    
    return ResultGenerator.genSuccessResult(progress);
}
```

## 项目结构

### 后端项目结构

```
intellibook-mall-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/intellibook/mall/
│   │   │       ├── api/                    # API 控制器
│   │   │       │   ├── admin/              # 管理后台 API
│   │   │       │   │   ├── AdminEBookAPI.java
│   │   │       │   │   ├── AdminOrderAPI.java
│   │   │       │   │   └── AdminUserAPI.java
│   │   │       │   └── mall/               # 用户端 API
│   │   │       │       ├── EBookAPI.java
│   │   │       │       ├── OrderAPI.java
│   │   │       │       ├── ReadingAPI.java
│   │   │       │       ├── RecommendationAPI.java
│   │   │       │       └── ReviewAPI.java
│   │   │       ├── common/                 # 公共类
│   │   │       │   ├── Constants.java
│   │   │       │   ├── ServiceResultEnum.java
│   │   │       │   └── IntelliBookException.java
│   │   │       ├── config/                 # 配置类
│   │   │       │   ├── WebMvcConfig.java
│   │   │       │   ├── ExceptionHandler.java
│   │   │       │   └── SpringDocConfig.java
│   │   │       ├── dao/                    # 数据访问层
│   │   │       │   ├── EBookMapper.java
│   │   │       │   ├── OrderMapper.java
│   │   │       │   ├── UserBehaviorMapper.java
│   │   │       │   ├── ReadingProgressMapper.java
│   │   │       │   └── ReviewMapper.java
│   │   │       ├── entity/                 # 实体类
│   │   │       │   ├── EBook.java
│   │   │       │   ├── EBookOrder.java
│   │   │       │   ├── MallUser.java
│   │   │       │   ├── UserBehavior.java
│   │   │       │   ├── ReadingProgress.java
│   │   │       │   └── EBookReview.java
│   │   │       ├── service/                # 服务接口
│   │   │       │   ├── EBookService.java
│   │   │       │   ├── OrderService.java
│   │   │       │   ├── ReadingService.java
│   │   │       │   ├── RecommendationService.java
│   │   │       │   └── ReviewService.java
│   │   │       ├── service/impl/           # 服务实现
│   │   │       │   ├── EBookServiceImpl.java
│   │   │       │   ├── OrderServiceImpl.java
│   │   │       │   ├── ReadingServiceImpl.java
│   │   │       │   ├── RecommendationServiceImpl.java
│   │   │       │   └── ReviewServiceImpl.java
│   │   │       ├── util/                   # 工具类
│   │   │       │   ├── MD5Util.java
│   │   │       │   ├── PageQueryUtil.java
│   │   │       │   ├── PageResult.java
│   │   │       │   └── ResultGenerator.java
│   │   │       └── IntelliBookApplication.java
│   │   └── resources/
│   │       ├── mapper/                     # MyBatis 映射文件
│   │       │   ├── EBookMapper.xml
│   │       │   ├── OrderMapper.xml
│   │       │   └── ...
│   │       ├── application.properties      # 配置文件
│   │       └── intellibook_mall_schema.sql # 数据库脚本
│   └── test/                               # 测试代码
│       └── java/
│           └── com/intellibook/mall/
│               ├── service/
│               └── api/
├── pom.xml                                 # Maven 配置
└── README.md                               # 项目说明
```

### 前端项目结构

```
intellibook-mall-vue/
├── src/
│   ├── api/                    # API 接口封装
│   │   ├── ebook.js
│   │   ├── order.js
│   │   ├── reading.js
│   │   └── recommendation.js
│   ├── assets/                 # 静态资源
│   │   ├── images/
│   │   └── styles/
│   ├── components/             # 公共组件
│   │   ├── EBookCard.vue
│   │   ├── PDFReader.vue
│   │   ├── EPUBReader.vue
│   │   └── RecommendationList.vue
│   ├── views/                  # 页面组件
│   │   ├── Home.vue
│   │   ├── EBookDetail.vue
│   │   ├── Search.vue
│   │   ├── Reading.vue
│   │   ├── Cart.vue
│   │   ├── Order.vue
│   │   └── User/
│   │       ├── Profile.vue
│   │       ├── MyBooks.vue
│   │       └── Favorites.vue
│   ├── router/                 # 路由配置
│   │   └── index.js
│   ├── store/                  # Vuex 状态管理
│   │   ├── modules/
│   │   │   ├── user.js
│   │   │   ├── cart.js
│   │   │   └── reading.js
│   │   └── index.js
│   ├── utils/                  # 工具函数
│   │   ├── request.js
│   │   └── auth.js
│   ├── App.vue
│   └── main.js
├── public/
│   └── index.html
├── package.json
└── vue.config.js
```

## 开发计划

### 第一阶段：基础架构搭建 (2周)

1. 项目初始化
   - 创建 Spring Boot 项目
   - 配置 MyBatis 和数据库连接
   - 搭建基础项目结构

2. 数据库设计
   - 创建所有数据表
   - 设计索引和约束
   - 准备测试数据

3. 基础功能开发
   - 用户注册登录
   - Token 认证机制
   - 统一异常处理
   - 统一响应格式

### 第二阶段：核心功能开发 (4周)

1. 电子书管理模块
   - 电子书 CRUD
   - 文件上传管理
   - 分类和标签管理
   - 搜索功能

2. 订单模块
   - 购物车功能
   - 订单创建和支付
   - 订单管理

3. 管理后台
   - 管理员登录
   - 电子书管理界面
   - 订单管理界面
   - 用户管理界面

### 第三阶段：高级功能开发 (3周)

1. 阅读模块
   - 在线阅读器集成
   - 阅读进度记录
   - 下载管理

2. 推荐系统
   - 用户行为追踪
   - 推荐算法实现
   - 推荐结果展示

3. 评价和收藏
   - 评价系统
   - 收藏功能

### 第四阶段：测试和优化 (2周)

1. 功能测试
2. 性能优化
3. 安全加固
4. 文档完善

## 总结

IntelliBook-Mall 基于 newbee-mall 的成熟架构，针对电子书业务进行了深度定制。主要改造包括：

**保留的核心架构**:
- Spring Boot + MyBatis 技术栈
- 前后端分离设计
- RESTful API 规范
- Token 认证机制
- 用户和订单基础模块

**重大改造**:
- 商品模块 → 电子书模块（增加元数据管理）
- 删除物流相关功能
- 新增在线阅读功能
- 新增智能推荐系统
- 新增评价和收藏功能
- 增强搜索功能

**技术亮点**:
- 混合推荐算法
- 在线阅读器集成
- 文件安全管理
- 用户行为追踪
- 阅读进度同步

该设计文档为后续开发提供了清晰的技术路线和实现方案，确保项目能够高质量完成。
