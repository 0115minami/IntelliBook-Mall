# IntelliBook-Mall 实施任务列表

本任务列表基于需求文档和设计文档，将项目分解为可执行的开发任务。每个任务都标注了对应的需求编号。

## 任务说明

- 核心实现任务必须完成
- 标记 `*` 的任务为可选任务（如单元测试、文档等）
- 每个任务应该是独立可测试的功能单元
- 任务按照依赖关系排序，建议按顺序执行

---

## 第一阶段：项目基础架构

- [ ] 1. 项目初始化和基础配置
  - [ ] 1.1 创建 Spring Boot 项目骨架
    - 基于 newbee-mall 项目结构创建新项目
    - 修改包名为 com.intellibook.mall
    - 配置 pom.xml 依赖（Spring Boot 3.3.x/3.4.x 推荐，MyBatis 3.5.x, MySQL 8.0+, Lombok 1.18.x, SpringDoc 2.x）
    - **版本说明**：以上版本号仅供参考，请根据项目具体需求选择合适版本
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 1.2 配置数据库连接
    - 创建 application.properties 配置文件
    - 配置 MySQL 数据源
    - 配置 MyBatis 映射文件路径
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 1.3 创建数据库和表结构
    - 编写 intellibook_mall_schema.sql 脚本
    - 创建用户表 tb_mall_user（增加 email, login_fail_count, lock_time 字段）
    - 创建电子书表 tb_ebook（新增 isbn, publisher, file_format 等字段）
    - 创建订单表 tb_ebook_order（移除地址相关字段）
    - 创建订单项表 tb_ebook_order_item（移除数量字段）
    - 创建购物车表 tb_ebook_shopping_cart_item（移除数量字段）
    - 创建用户行为表 tb_user_behavior（新表）
    - 创建阅读进度表 tb_reading_progress（新表）
    - 创建评价表 tb_ebook_review（新表）
    - 创建收藏表 tb_ebook_favorite（新表）
    - 创建标签表 tb_ebook_tag（新表）
    - 创建标签关联表 tb_ebook_tag_relation（新表）
    - 创建分类表 tb_ebook_category（复用 newbee-mall 结构）
    - 创建轮播图表 tb_carousel（复用 newbee-mall 结构）
    - 创建管理员表 tb_admin_user（复用 newbee-mall 结构）
    - 创建 Token 表 tb_mall_user_token 和 tb_admin_user_token（复用 newbee-mall 结构）
    - _需求: 所有需求的数据基础_
  
  - [ ] 1.4 搭建项目目录结构
    - 创建 api、common、config、dao、entity、service、util 包
    - 创建 admin 和 mall 子包用于区分管理后台和用户端 API
    - _需求: 系统性能与安全 (需求14)_

- [ ] 2. 基础工具类和配置类
  - [ ] 2.1 复制并调整 newbee-mall 的工具类
    - 复制 MD5Util、PageQueryUtil、PageResult、Result、ResultGenerator
    - 复制 BeanUtil、NumberUtil、SystemUtil
    - 确保所有工具类适配新项目
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 2.2 创建统一异常处理
    - 创建 IntelliBookException 自定义异常类
    - 创建 ServiceResultEnum 枚举（增加电子书相关错误码）
    - 创建 IntelliBookExceptionHandler 全局异常处理器
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 2.3 配置 Web MVC
    - 创建 WebMvcConfig 配置类
    - 配置跨域 CORS
    - 配置静态资源映射
    - 配置拦截器（Token 认证拦截器）
    - _需求: 用户注册与登录 (需求1)_
  
  - [ ] 2.4 配置 SpringDoc (OpenAPI)
    - 创建 SpringDocConfig 配置类
    - 配置 API 文档信息
    - 配置认证方式（Token Header）
    - _需求: 系统性能与安全 (需求14)_

---

## 第二阶段：用户模块

- [ ] 3. 用户注册和登录功能
  - [ ] 3.1 创建用户实体和 Mapper
    - 创建 MallUser 实体类（增加 email, loginFailCount, lockTime 字段）
    - 创建 MallUserToken 实体类
    - 创建 MallUserMapper 接口
    - 编写 MallUserMapper.xml 映射文件（增加邮箱查询、登录失败次数更新等方法）
    - _需求: 用户注册与登录 (需求1)_
  
  - [ ] 3.2 实现用户 Service 层
    - 创建 MallUserService 接口
    - 实现 MallUserServiceImpl（包含注册、登录、Token 生成、登录失败锁定逻辑）
    - 实现密码 MD5 加密
    - 实现登录失败次数统计和账户锁定（5次失败锁定30分钟）
    - _需求: 用户注册与登录 (需求1)_
  
  - [ ] 3.3 实现用户 API 接口
    - 创建 MallUserAPI 控制器
    - 实现 POST /api/v1/user/register 注册接口
    - 实现 POST /api/v1/user/login 登录接口
    - 实现 POST /api/v1/user/logout 登出接口
    - 实现 GET /api/v1/user/info 获取用户信息接口
    - 实现 PUT /api/v1/user/info 更新用户信息接口
    - _需求: 用户注册与登录 (需求1)_
  
  - [ ]* 3.4 编写用户模块单元测试
    - 测试用户注册功能
    - 测试用户登录功能
    - 测试登录失败锁定功能
    - 测试 Token 生成和验证
    - _需求: 用户注册与登录 (需求1)_


---

## 第三阶段：电子书管理模块

- [ ] 4. 电子书基础功能
  - [ ] 4.1 创建电子书实体和 Mapper
    - 创建 EBook 实体类（包含 isbn, publisher, fileFormat, filePath 等新字段）
    - 创建 EBookCategory 实体类（复用 newbee-mall 的分类结构）
    - 创建 EBookTag 和 EBookTagRelation 实体类
    - 创建 EBookMapper 接口
    - 编写 EBookMapper.xml 映射文件（包含基础 CRUD、搜索、按作者/ISBN查询等方法）
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_
  
  - [ ] 4.2 实现电子书 Service 层
    - 创建 EBookService 接口
    - 实现 EBookServiceImpl（包含 CRUD、搜索、标签管理等功能）
    - 实现分页查询功能
    - 实现高级搜索功能（多条件组合查询）
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_
  
  - [ ] 4.3 实现电子书 API 接口
    - 创建 EBookAPI 控制器
    - 实现 GET /api/v1/ebooks 获取电子书列表接口
    - 实现 GET /api/v1/ebooks/{id} 获取电子书详情接口
    - 实现 GET /api/v1/ebooks/search 搜索接口
    - 实现 GET /api/v1/ebooks/advanced-search 高级搜索接口
    - 实现 GET /api/v1/ebooks/by-author 按作者查询接口
    - 实现 GET /api/v1/ebooks/by-isbn 按ISBN查询接口
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3), 电子书详情展示 (需求4)_
  
  - [ ]* 4.4 编写电子书模块单元测试
    - 测试电子书 CRUD 功能
    - 测试搜索功能
    - 测试高级搜索功能
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_

- [ ] 5. 电子书文件管理（多格式支持）
  - [ ] 5.1 创建电子书文件实体和 Mapper
    - 创建 EBookFile 实体类
    - 创建 EBookFileMapper 接口
    - 编写 EBookFileMapper.xml 映射文件（包含按书籍查询、按格式查询等方法）
    - _需求: 电子书库管理 (需求2)_
  
  - [ ] 5.2 实现电子书文件 Service 层
    - 创建 EBookFileService 接口
    - 实现 EBookFileServiceImpl
    - 实现文件上传功能（支持 PDF、EPUB、MOBI、AZW3 格式）
    - 实现文件删除功能
    - 实现获取书籍所有格式文件功能
    - 实现获取指定格式文件功能
    - 实现文件格式验证
    - 实现下载次数统计
    - _需求: 电子书库管理 (需求2)_
  
  - [ ] 5.3 实现文件存储功能
    - 创建文件存储目录结构（covers/, books/pdf/, books/epub/, books/mobi/, books/azw3/）
    - 实现文件上传工具类
    - 实现文件大小和格式验证
    - 实现文件重命名和路径生成
    - _需求: 电子书库管理 (需求2)_
  
  - [ ] 5.4 实现封面图片上传
    - 实现图片上传功能
    - 实现图片压缩和缩略图生成
    - 实现图片格式验证（JPG、PNG）
    - _需求: 电子书库管理 (需求2)_
  
  - [ ] 5.5 实现文件管理 API
    - 实现 POST /api/v1/admin/ebooks/{id}/files 上传电子书文件接口（支持多格式）
    - 实现 GET /api/v1/ebooks/{id}/files 获取电子书所有格式接口
    - 实现 DELETE /api/v1/admin/ebooks/files/{fileId} 删除文件接口
    - 实现 POST /api/v1/admin/ebooks/upload-cover 封面上传接口
    - _需求: 电子书库管理 (需求2)_
  
  - [ ] 5.6 修改电子书 Service 层以支持多格式
    - 修改 getEBookById 方法，自动加载所有格式文件
    - 修改电子书列表查询，可选加载文件信息
    - 更新电子书详情 VO 对象，包含文件列表
    - _需求: 电子书库管理 (需求2)_

- [ ] 6. 分类和标签管理
  - [ ] 6.1 实现分类管理
    - 创建 EBookCategoryService 接口和实现类
    - 实现分类树查询功能
    - 实现分类 CRUD 功能
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_
  
  - [ ] 6.2 实现标签管理
    - 创建 EBookTagService 接口和实现类
    - 实现标签 CRUD 功能
    - 实现电子书-标签关联管理
    - 实现标签使用次数统计
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_
  
  - [ ] 6.3 实现分类和标签 API
    - 实现 GET /api/v1/categories 获取分类列表接口
    - 实现 GET /api/v1/categories/{id}/ebooks 获取分类下电子书接口
    - 实现 GET /api/v1/ebooks/{id}/tags 获取电子书标签接口
    - _需求: 电子书库管理 (需求2), 高级搜索功能 (需求3)_


---

## 第四阶段：购物车和订单模块

- [ ] 7. 购物车功能
  - [ ] 7.1 创建购物车实体和 Mapper
    - 创建 EBookShoppingCartItem 实体类（移除 goodsCount 字段）
    - 创建 EBookShoppingCartItemMapper 接口
    - 编写 EBookShoppingCartItemMapper.xml 映射文件
    - _需求: 购物车功能 (需求7)_
  
  - [ ] 7.2 实现购物车 Service 层
    - 创建 EBookShoppingCartService 接口
    - 实现 EBookShoppingCartServiceImpl
    - 实现添加到购物车功能（检查是否已购买）
    - 实现删除购物车项功能
    - 实现获取购物车列表功能
    - 实现购物车总价计算
    - _需求: 购物车功能 (需求7)_
  
  - [ ] 7.3 实现购物车 API 接口
    - 创建 EBookShoppingCartAPI 控制器
    - 实现 GET /api/v1/cart 获取购物车接口
    - 实现 POST /api/v1/cart 添加到购物车接口
    - 实现 DELETE /api/v1/cart/{id} 删除购物车项接口
    - 实现 GET /api/v1/cart/check-purchased/{bookId} 检查是否已购买接口
    - _需求: 购物车功能 (需求7)_
  
  - [ ]* 7.4 编写购物车模块单元测试
    - 测试添加到购物车功能
    - 测试删除购物车项功能
    - 测试已购买检查功能
    - _需求: 购物车功能 (需求7)_

- [ ] 8. 订单功能
  - [ ] 8.1 创建订单实体和 Mapper
    - 创建 EBookOrder 实体类（移除地址相关字段）
    - 创建 EBookOrderItem 实体类（移除数量字段）
    - 创建 EBookOrderMapper 和 EBookOrderItemMapper 接口
    - 编写 Mapper.xml 映射文件
    - _需求: 订单管理 (需求8)_
  
  - [ ] 8.2 实现订单 Service 层
    - 创建 EBookOrderService 接口
    - 实现 EBookOrderServiceImpl
    - 实现订单创建功能（从购物车生成订单）
    - 实现订单号生成逻辑
    - 实现订单状态管理（待支付、已支付、已取消）
    - 实现订单自动取消功能（30分钟未支付）
    - 实现电子书访问权限授予功能
    - _需求: 订单管理 (需求8)_
  
  - [ ] 8.3 实现支付功能
    - 实现支付成功回调处理
    - 实现订单状态更新
    - 实现电子书访问权限授予
    - 实现购物车清空
    - _需求: 订单管理 (需求8)_
  
  - [ ] 8.4 实现订单 API 接口
    - 创建 EBookOrderAPI 控制器
    - 实现 POST /api/v1/orders 创建订单接口
    - 实现 GET /api/v1/orders 获取订单列表接口
    - 实现 GET /api/v1/orders/{orderNo} 获取订单详情接口
    - 实现 PUT /api/v1/orders/{orderNo}/cancel 取消订单接口
    - 实现 PUT /api/v1/orders/{orderNo}/pay 支付订单接口
    - 实现 GET /api/v1/orders/purchased-ebooks 获取已购电子书接口
    - _需求: 订单管理 (需求8)_
  
  - [ ]* 8.5 编写订单模块单元测试
    - 测试订单创建功能
    - 测试订单支付功能
    - 测试订单取消功能
    - 测试权限授予功能
    - _需求: 订单管理 (需求8)_


---

## 第五阶段：阅读模块

- [ ] 9. 在线阅读功能
  - [ ] 9.1 创建阅读进度实体和 Mapper
    - 创建 ReadingProgress 实体类
    - 创建 ReadingProgressMapper 接口
    - 编写 ReadingProgressMapper.xml 映射文件
    - _需求: 在线阅读功能 (需求5)_
  
  - [ ] 9.2 实现阅读 Service 层
    - 创建 ReadingService 接口
    - 实现 ReadingServiceImpl
    - 实现阅读权限验证（检查是否已购买）
    - 实现阅读进度保存功能
    - 实现阅读进度查询功能
    - 实现最近阅读列表功能
    - _需求: 在线阅读功能 (需求5)_
  
  - [ ] 9.3 实现电子书内容获取
    - 实现 PDF 文件读取功能
    - 实现 EPUB 文件读取功能
    - 实现文件流返回
    - 实现试读功能（前10页或5%内容）
    - _需求: 在线阅读功能 (需求5), 电子书详情展示 (需求4)_
  
  - [ ] 9.4 实现阅读 API 接口
    - 创建 ReadingAPI 控制器
    - 实现 GET /api/v1/reading/{bookId}/content 获取电子书内容接口
    - 实现 GET /api/v1/reading/{bookId}/progress 获取阅读进度接口
    - 实现 POST /api/v1/reading/{bookId}/progress 保存阅读进度接口
    - 实现 GET /api/v1/reading/recent 获取最近阅读接口
    - _需求: 在线阅读功能 (需求5)_
  
  - [ ]* 9.5 编写阅读模块单元测试
    - 测试阅读权限验证
    - 测试阅读进度保存和查询
    - 测试试读功能
    - _需求: 在线阅读功能 (需求5)_

- [ ] 10. 下载管理功能（支持格式选择）
  - [ ] 10.1 创建下载记录实体和 Mapper
    - 创建 DownloadRecord 实体类（包含 fileId 和 format 字段）
    - 创建 DownloadRecordMapper 接口
    - 编写 DownloadRecordMapper.xml 映射文件
    - _需求: 电子书下载功能 (需求6)_
  
  - [ ] 10.2 实现下载管理 Service 层
    - 在 ReadingService 中添加下载相关方法
    - 实现下载权限验证
    - 实现格式选择功能（用户可选择下载 PDF、EPUB、MOBI 等格式）
    - 实现临时下载链接生成（包含 fileId、签名和过期时间）
    - 实现下载次数限制检查
    - 实现下载记录保存（记录下载的具体格式）
    - 集成 EBookFileService 获取文件信息
    - _需求: 电子书下载功能 (需求6)_
  
  - [ ] 10.3 实现下载 API 接口
    - 在 ReadingAPI 中添加下载相关接口
    - 实现 POST /api/v1/reading/{bookId}/download 生成下载链接接口（支持 format 参数）
    - 实现 GET /api/v1/reading/{bookId}/download-record 获取下载记录接口
    - 实现 GET /api/v1/download 文件下载接口（验证签名和权限，支持多格式）
    - 实现下载时自动更新对应格式的下载次数
    - _需求: 电子书下载功能 (需求6)_
  
  - [ ]* 10.4 编写下载模块单元测试
    - 测试下载权限验证
    - 测试格式选择功能
    - 测试临时链接生成和验证
    - 测试下载次数限制
    - 测试多格式下载统计
    - _需求: 电子书下载功能 (需求6)_


---

## 第六阶段：推荐系统

- [ ] 11. 用户行为追踪
  - [ ] 11.1 创建用户行为实体和 Mapper
    - 创建 UserBehavior 实体类
    - 创建 UserBehaviorMapper 接口
    - 编写 UserBehaviorMapper.xml 映射文件（包含按类型查询、时间范围查询等方法）
    - _需求: 用户行为追踪 (需求10), 智能推荐系统 (需求9)_
  
  - [ ] 11.2 实现行为追踪 Service 层
    - 创建 UserBehaviorService 接口
    - 实现 UserBehaviorServiceImpl
    - 实现浏览行为记录
    - 实现搜索行为记录
    - 实现购买行为记录
    - 实现收藏行为记录
    - 实现行为数据清理（180天前数据）
    - _需求: 用户行为追踪 (需求10)_
  
  - [ ] 11.3 集成行为追踪到现有接口
    - 在电子书详情接口中添加浏览行为记录
    - 在搜索接口中添加搜索行为记录
    - 在订单支付接口中添加购买行为记录
    - _需求: 用户行为追踪 (需求10)_

- [ ] 12. 推荐算法实现
  - [ ] 12.1 实现推荐 Service 层
    - 创建 RecommendationService 接口
    - 实现 RecommendationServiceImpl
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 12.2 实现协同过滤推荐
    - 实现基于用户的协同过滤算法
    - 实现用户相似度计算（余弦相似度）
    - 实现推荐结果生成
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 12.3 实现基于内容的推荐
    - 实现电子书相似度计算（分类、标签、作者）
    - 实现内容推荐算法
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 12.4 实现热度推荐
    - 实现热度分数计算（购买、收藏、浏览、评分）
    - 实现时间衰减算法
    - 实现热门电子书排序
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 12.5 实现混合推荐策略
    - 实现多种推荐结果融合
    - 实现推荐权重配置
    - 实现推荐结果去重和排序
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 12.6 实现推荐 API 接口
    - 创建 RecommendationAPI 控制器
    - 实现 GET /api/v1/recommendations/personal 个性化推荐接口
    - 实现 GET /api/v1/recommendations/hot 热门推荐接口
    - 实现 GET /api/v1/recommendations/similar/{bookId} 相似推荐接口
    - 实现 GET /api/v1/recommendations/category/{categoryId} 分类推荐接口
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ]* 12.7 编写推荐模块单元测试
    - 测试协同过滤算法
    - 测试内容推荐算法
    - 测试热度计算
    - 测试混合推荐策略
    - _需求: 智能推荐系统 (需求9)_


---

## 第七阶段：评价和收藏模块

- [ ] 13. 评价系统
  - [ ] 13.1 创建评价实体和 Mapper
    - 创建 EBookReview 实体类
    - 创建 ReviewLike 实体类
    - 创建 EBookReviewMapper 和 ReviewLikeMapper 接口
    - 编写 Mapper.xml 映射文件
    - _需求: 电子书评价系统 (需求11)_
  
  - [ ] 13.2 实现评价 Service 层
    - 创建 EBookReviewService 接口
    - 实现 EBookReviewServiceImpl
    - 实现评价权限验证（必须购买后才能评价）
    - 实现评价 CRUD 功能
    - 实现评分统计和平均分计算
    - 实现点赞功能
    - 实现评价列表分页查询
    - _需求: 电子书评价系统 (需求11)_
  
  - [ ] 13.3 实现评价 API 接口
    - 创建 EBookReviewAPI 控制器
    - 实现 GET /api/v1/reviews/{bookId} 获取电子书评价接口
    - 实现 POST /api/v1/reviews 提交评价接口
    - 实现 PUT /api/v1/reviews/{id} 更新评价接口
    - 实现 DELETE /api/v1/reviews/{id} 删除评价接口
    - 实现 POST /api/v1/reviews/{id}/like 点赞评价接口
    - 实现 DELETE /api/v1/reviews/{id}/like 取消点赞接口
    - _需求: 电子书评价系统 (需求11)_
  
  - [ ] 13.4 集成评价到电子书详情
    - 在电子书详情接口中返回平均评分和评论数
    - 在支付成功后更新电子书评分统计
    - _需求: 电子书评价系统 (需求11), 电子书详情展示 (需求4)_
  
  - [ ]* 13.5 编写评价模块单元测试
    - 测试评价权限验证
    - 测试评价 CRUD 功能
    - 测试评分计算
    - 测试点赞功能
    - _需求: 电子书评价系统 (需求11)_

- [ ] 14. 收藏功能
  - [ ] 14.1 创建收藏实体和 Mapper
    - 创建 EBookFavorite 实体类
    - 创建 EBookFavoriteMapper 接口
    - 编写 EBookFavoriteMapper.xml 映射文件
    - _需求: 电子书收藏功能 (需求12)_
  
  - [ ] 14.2 实现收藏 Service 层
    - 创建 EBookFavoriteService 接口
    - 实现 EBookFavoriteServiceImpl
    - 实现添加收藏功能
    - 实现取消收藏功能
    - 实现收藏状态检查
    - 实现收藏列表分页查询
    - 实现收藏数量统计
    - _需求: 电子书收藏功能 (需求12)_
  
  - [ ] 14.3 实现收藏 API 接口
    - 创建 EBookFavoriteAPI 控制器
    - 实现 GET /api/v1/favorites 获取收藏列表接口
    - 实现 POST /api/v1/favorites/{bookId} 添加收藏接口
    - 实现 DELETE /api/v1/favorites/{bookId} 取消收藏接口
    - 实现 GET /api/v1/favorites/check/{bookId} 检查是否收藏接口
    - _需求: 电子书收藏功能 (需求12)_
  
  - [ ] 14.4 集成收藏行为追踪
    - 在添加收藏时记录用户行为
    - 在取消收藏时更新用户行为
    - _需求: 电子书收藏功能 (需求12), 用户行为追踪 (需求10)_
  
  - [ ]* 14.5 编写收藏模块单元测试
    - 测试添加收藏功能
    - 测试取消收藏功能
    - 测试收藏列表查询
    - _需求: 电子书收藏功能 (需求12)_

- [ ] 15. 用户兴趣管理
  - [ ] 15.1 创建用户兴趣实体和 Mapper
    - 创建 UserInterest 实体类
    - 创建 UserInterestMapper 接口
    - 编写 UserInterestMapper.xml 映射文件（包含按用户查询、按分类查询等方法）
    - _需求: 用户兴趣管理 (需求13)_
  
  - [ ] 15.2 实现用户兴趣 Service 层
    - 创建 UserInterestService 接口
    - 实现 UserInterestServiceImpl
    - 实现添加兴趣功能（单个和批量）
    - 实现删除兴趣功能
    - 实现获取用户兴趣列表功能
    - 实现兴趣状态检查功能
    - 实现基于兴趣的推荐功能
    - _需求: 用户兴趣管理 (需求13)_
  
  - [ ] 15.3 实现用户兴趣 API 接口
    - 创建 UserInterestAPI 控制器
    - 实现 GET /api/v1/user/interests 获取用户兴趣列表接口
    - 实现 POST /api/v1/user/interests 添加兴趣接口
    - 实现 POST /api/v1/user/interests/batch 批量添加兴趣接口
    - 实现 DELETE /api/v1/user/interests/{categoryId} 删除兴趣接口
    - 实现 GET /api/v1/user/interests/check/{categoryId} 检查兴趣状态接口
    - 实现 GET /api/v1/user/interests/recommendations 基于兴趣的推荐接口
    - _需求: 用户兴趣管理 (需求13)_
  
  - [ ] 15.4 集成兴趣到注册流程
    - 在用户注册成功后引导选择兴趣
    - 创建兴趣选择引导页面接口
    - 实现新用户兴趣初始化
    - _需求: 用户兴趣管理 (需求13)_
  
  - [ ] 15.5 增强推荐算法
    - 在个性化推荐中整合用户兴趣
    - 实现兴趣权重计算
    - 优化冷启动推荐策略
    - _需求: 用户兴趣管理 (需求13), 智能推荐系统 (需求9)_
  
  - [ ]* 15.6 编写用户兴趣模块单元测试
    - 测试添加兴趣功能
    - 测试删除兴趣功能
    - 测试基于兴趣的推荐
    - 测试冷启动场景
    - _需求: 用户兴趣管理 (需求13)_


---

## 第八阶段：管理后台（可选阶段）

**注意**: 此阶段为可选功能，如果暂时不需要管理后台，可以跳过此阶段，使用简化方案管理电子书数据。

- [ ]* 16. 管理员功能
  - [ ]* 16.1 创建管理员实体和 Mapper
    - 创建 AdminUser 实体类（复用 newbee-mall 结构）
    - 创建 AdminUserToken 实体类
    - 创建 AdminUserMapper 接口
    - 编写 AdminUserMapper.xml 映射文件
    - _需求: 管理员后台 (需求14)_
  
  - [ ]* 16.2 实现管理员 Service 层
    - 创建 AdminUserService 接口
    - 实现 AdminUserServiceImpl
    - 实现管理员登录功能
    - 实现 Token 认证
    - _需求: 管理员后台 (需求14)_
  
  - [ ]* 16.3 实现管理员 API 接口
    - 创建 AdminUserAPI 控制器
    - 实现 POST /api/v1/admin/login 管理员登录接口
    - 实现 POST /api/v1/admin/logout 管理员登出接口
    - 实现 GET /api/v1/admin/profile 获取管理员信息接口
    - _需求: 管理员后台 (需求14)_

- [ ]* 17. 管理后台电子书管理
  - [ ]* 16.1 实现管理后台电子书 API
    - 创建 AdminEBookAPI 控制器
    - 实现 GET /api/v1/admin/ebooks 获取电子书列表接口（分页、搜索）
    - 实现 POST /api/v1/admin/ebooks 添加电子书接口
    - 实现 PUT /api/v1/admin/ebooks/{id} 更新电子书接口
    - 实现 DELETE /api/v1/admin/ebooks/{id} 删除电子书接口（软删除）
    - 实现 PUT /api/v1/admin/ebooks/status 批量上下架接口
    - _需求: 管理员后台 (需求13), 电子书库管理 (需求2)_
  
  - [ ]* 16.2 实现批量导入功能
    - 实现 CSV 格式电子书数据导入
    - 实现 JSON 格式电子书数据导入
    - 实现导入数据验证
    - 实现 POST /api/v1/admin/ebooks/batch-import 批量导入接口
    - _需求: 管理员后台 (需求13), 电子书库管理 (需求2)_

- [ ]* 18. 管理后台订单管理
  - [ ]* 17.1 实现管理后台订单 API
    - 创建 AdminOrderAPI 控制器
    - 实现 GET /api/v1/admin/orders 获取订单列表接口（分页、搜索、筛选）
    - 实现 GET /api/v1/admin/orders/{id} 获取订单详情接口
    - 实现 PUT /api/v1/admin/orders/{id} 更新订单信息接口
    - 实现 PUT /api/v1/admin/orders/close 批量关闭订单接口
    - 实现 GET /api/v1/admin/orders/export 导出订单数据接口
    - _需求: 管理员后台 (需求13)_

- [ ]* 19. 管理后台用户管理
  - [ ]* 18.1 实现管理后台用户 API
    - 创建 AdminMallUserAPI 控制器
    - 实现 GET /api/v1/admin/users 获取用户列表接口（分页、搜索）
    - 实现 GET /api/v1/admin/users/{id} 获取用户详情接口
    - 实现 PUT /api/v1/admin/users/{id}/lock 锁定/解锁用户接口
    - 实现 GET /api/v1/admin/users/{id}/orders 获取用户订单历史接口
    - _需求: 管理员后台 (需求13)_

- [ ]* 20. 管理后台配置管理
  - [ ]* 19.1 实现轮播图管理
    - 创建 Carousel 实体类（复用 newbee-mall 结构）
    - 创建 CarouselMapper 接口
    - 创建 CarouselService 接口和实现类
    - 创建 AdminCarouselAPI 控制器
    - 实现轮播图 CRUD 接口
    - _需求: 管理员后台 (需求13)_
  
  - [ ]* 19.2 实现首页推荐位配置
    - 创建 IndexConfig 实体类（复用 newbee-mall 结构）
    - 创建 IndexConfigMapper 接口
    - 创建 IndexConfigService 接口和实现类
    - 创建 AdminIndexConfigAPI 控制器
    - 实现推荐位 CRUD 接口
    - _需求: 管理员后台 (需求13)_

- [ ]* 21. 管理后台数据统计
  - [ ]* 20.1 实现统计数据查询
    - 实现销售额统计（按日、周、月）
    - 实现订单数统计
    - 实现用户数统计
    - 实现热门电子书统计（按销量、浏览量）
    - 实现分类销售统计
    - _需求: 管理员后台 (需求13)_
  
  - [ ]* 20.2 实现统计 API 接口
    - 创建 AdminStatisticsAPI 控制器
    - 实现 GET /api/v1/admin/statistics 获取统计数据接口
    - 实现 GET /api/v1/admin/statistics/sales 获取销售统计接口
    - 实现 GET /api/v1/admin/statistics/hot-books 获取热门书籍接口
    - _需求: 管理员后台 (需求13)_


---

## 第九阶段：前端集成和优化

- [ ] 22. 首页和导航
  - [ ] 21.1 实现首页 API 接口
    - 创建 IndexAPI 控制器
    - 实现 GET /api/v1/index/carousels 获取轮播图接口
    - 实现 GET /api/v1/index/recommendations 获取首页推荐接口
    - 实现 GET /api/v1/index/hot-books 获取热门电子书接口
    - 实现 GET /api/v1/index/new-books 获取新书上架接口
    - _需求: 智能推荐系统 (需求9)_
  
  - [ ] 21.2 实现分类导航接口
    - 实现 GET /api/v1/categories/tree 获取分类树接口
    - 实现分类层级结构返回
    - _需求: 高级搜索功能 (需求3)_

- [ ] 23. 性能优化
  - [ ] 22.1 数据库优化
    - 为常用查询字段添加索引（isbn, author, category_id, user_id 等）
    - 优化慢查询 SQL
    - 实现分页查询优化
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 22.2 接口响应优化
    - 实现 VO 对象转换（避免返回过多字段）
    - 实现接口响应数据压缩
    - 优化 N+1 查询问题
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ]* 22.3 缓存优化（可选）
    - 集成 Redis
    - 实现热门电子书缓存
    - 实现分类树缓存
    - 实现推荐结果缓存
    - _需求: 系统性能与安全 (需求14)_

- [ ] 24. 安全加固
  - [ ] 23.1 实现 Token 刷新机制
    - 实现 Token 过期检查
    - 实现 Token 自动刷新
    - 实现 POST /api/v1/user/refresh-token 刷新 Token 接口
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 23.2 实现接口限流
    - 实现基于 IP 的限流
    - 实现基于用户的限流
    - 实现敏感接口保护（登录、支付等）
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 23.3 实现审计日志
    - 创建 AuditLog 实体类
    - 实现敏感操作日志记录（登录、支付、删除等）
    - 实现日志查询接口
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ] 23.4 实现数据脱敏
    - 实现用户手机号脱敏
    - 实现邮箱地址脱敏
    - 实现订单信息脱敏
    - _需求: 系统性能与安全 (需求14)_

---

## 第十阶段：测试和文档

- [ ]* 25. 集成测试
  - [ ]* 24.1 编写 API 集成测试
    - 测试用户注册登录流程
    - 测试电子书搜索和详情查询
    - 测试购物车和订单流程
    - 测试阅读和下载功能
    - 测试推荐接口
    - _需求: 所有核心需求_
  
  - [ ]* 24.2 编写性能测试
    - 使用 JMeter 进行压力测试
    - 测试首页加载性能
    - 测试搜索接口性能
    - 测试并发订单创建
    - 测试文件下载并发
    - _需求: 系统性能与安全 (需求14)_

- [ ]* 26. 文档编写
  - [ ]* 25.1 完善 API 文档
    - 使用 SpringDoc 生成 OpenAPI 文档
    - 为所有接口添加详细注释
    - 添加请求示例和响应示例
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ]* 25.2 编写部署文档
    - 编写环境配置说明
    - 编写数据库初始化步骤
    - 编写应用部署步骤
    - 编写常见问题解决方案
    - _需求: 系统性能与安全 (需求14)_
  
  - [ ]* 25.3 编写用户手册
    - 编写用户端功能说明
    - 编写管理后台使用说明
    - 添加功能截图和操作步骤
    - _需求: 所有用户相关需求_

---

## 任务统计

**总任务数**: 26 个主任务
**核心任务数**: 约 160 个子任务（不含可选任务）
**可选任务数**: 约 21 个子任务（标记 * 的任务）

**预计开发时间**: 
- 第一阶段（项目基础）: 1-2 周
- 第二阶段（用户模块）: 1 周
- 第三阶段（电子书管理）: 2 周
- 第四阶段（购物车订单）: 2 周
- 第五阶段（阅读模块）: 2 周
- 第六阶段（推荐系统）: 2-3 周
- 第七阶段（评价收藏兴趣）: 1-2 周
- 第八阶段（管理后台）: 2 周
- 第九阶段（优化）: 1-2 周
- 第十阶段（测试文档）: 1 周

**总计**: 约 15-18 周（3.5-4.5 个月）

## 开发建议

1. **按阶段顺序开发**: 每个阶段的任务有依赖关系，建议按顺序完成
2. **先核心后可选**: 优先完成核心功能，可选任务可以根据时间安排
3. **增量开发**: 每完成一个模块就进行测试，确保功能正常
4. **代码复用**: 充分利用 newbee-mall 的代码，减少重复开发
5. **持续集成**: 建议使用 Git 进行版本控制，每完成一个任务提交一次
6. **文档同步**: 在开发过程中同步更新文档，避免最后集中编写

## 注意事项

1. 所有涉及文件操作的功能都要注意安全性（路径验证、权限检查）
2. 所有涉及金额的字段使用整数（分）存储，避免浮点数精度问题
3. 所有删除操作使用软删除（is_deleted 标识）
4. 所有时间字段使用 datetime 类型，统一使用 GMT+8 时区
5. 所有接口都要进行参数验证和异常处理
6. 敏感操作（支付、删除等）要记录审计日志
7. 推荐算法可以先实现简单版本，后续再优化
8. 在线阅读器可以先支持 PDF，EPUB 支持可以后续添加
