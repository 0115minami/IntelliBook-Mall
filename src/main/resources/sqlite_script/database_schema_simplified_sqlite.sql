-- ============================================
-- IntelliBook-Mall 简化版数据库设计（SQLite）
-- 适用于毕业设计项目
-- ============================================

-- 1. 用户表（简化版）
CREATE TABLE IF NOT EXISTS tb_user (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    is_admin TINYINT DEFAULT 0,  -- 0-普通用户 1-管理员
    locked_flag TINYINT DEFAULT 0,  -- 0-未锁定 1-已锁定
    login_fail_count INTEGER DEFAULT 0,  -- 登录失败次数
    lock_time DATETIME,  -- 锁定时间
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. 电子书分类表（简化版）
CREATE TABLE IF NOT EXISTS tb_category (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name VARCHAR(50) NOT NULL,
    category_name_en VARCHAR(50),
    parent_id INTEGER DEFAULT 0,
    category_level TINYINT DEFAULT 1,  -- 1-一级 2-二级
    sort_order INTEGER DEFAULT 0,
    FOREIGN KEY (parent_id) REFERENCES tb_category(category_id)
);

-- 3. 电子书表（核心表 - 元数据）
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
    page_count INTEGER DEFAULT 0,
    price INTEGER DEFAULT 0,  -- 价格（分）
    language VARCHAR(10) DEFAULT 'zh-CN',  -- 语言
    tags VARCHAR(500),  -- 标签（逗号分隔，如"Java,编程,入门"）
    rating REAL DEFAULT 0.0,  -- 评分（0-5）
    rating_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,  -- 浏览次数
    download_count INTEGER DEFAULT 0,  -- 下载次数
    status TINYINT DEFAULT 1,  -- 1-上架 0-下架
    is_deleted TINYINT DEFAULT 0,  -- 0-未删除 1-已删除（软删除）
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id)
);

-- 3.1 电子书文件表（新增 - 支持多格式）
CREATE TABLE IF NOT EXISTS tb_ebook_file (
    file_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,  -- PDF/EPUB/MOBI/AZW3等
    file_path VARCHAR(500) NOT NULL,
    file_size INTEGER,  -- 文件大小（字节）
    download_count INTEGER DEFAULT 0,  -- 该格式的下载次数
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE,
    UNIQUE(book_id, file_format)  -- 同一本书的同一格式只能有一个文件
);

-- 4. 订单表（简化版）
CREATE TABLE IF NOT EXISTS tb_order (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no VARCHAR(20) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL,
    total_price INTEGER NOT NULL,
    pay_status TINYINT DEFAULT 0,  -- 0-未支付 1-已支付
    pay_type TINYINT DEFAULT 0,  -- 0-未支付 1-支付宝 2-微信 3-余额
    pay_time DATETIME,
    order_status TINYINT DEFAULT 0,  -- 0-待支付 1-已完成 2-已取消
    extra_info VARCHAR(500),  -- 额外信息
    is_deleted TINYINT DEFAULT 0,  -- 0-未删除 1-已删除
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

-- 5. 订单项表
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

-- 6. 购物车表（简化版）
CREATE TABLE IF NOT EXISTS tb_cart (
    cart_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    is_deleted TINYINT DEFAULT 0,  -- 0-未删除 1-已删除
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)  -- 防止重复添加
);

-- 7. 收藏表（简化版）
CREATE TABLE IF NOT EXISTS tb_favorite (
    favorite_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)
);

-- 8. 评价表（简化版）
CREATE TABLE IF NOT EXISTS tb_review (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    rating TINYINT NOT NULL,  -- 1-5星
    content TEXT,
    like_count INTEGER DEFAULT 0,  -- 点赞数
    is_deleted TINYINT DEFAULT 0,  -- 0-未删除 1-已删除
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id)
);

-- 8.1 评价点赞表（新增）
CREATE TABLE IF NOT EXISTS tb_review_like (
    like_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    review_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (review_id) REFERENCES tb_review(review_id) ON DELETE CASCADE,
    UNIQUE(user_id, review_id)  -- 防止重复点赞
);

-- ============================================
-- 索引优化
-- ============================================

-- 电子书表索引
CREATE INDEX idx_ebook_category ON tb_ebook(category_id);
CREATE INDEX idx_ebook_language ON tb_ebook(language);
CREATE INDEX idx_ebook_author ON tb_ebook(author);
CREATE INDEX idx_ebook_status ON tb_ebook(status);

-- 电子书文件表索引
CREATE INDEX idx_ebook_file_book ON tb_ebook_file(book_id);
CREATE INDEX idx_ebook_file_format ON tb_ebook_file(file_format);

-- 订单表索引
CREATE INDEX idx_order_user ON tb_order(user_id);
CREATE INDEX idx_order_status ON tb_order(order_status);

-- 购物车索引
CREATE INDEX idx_cart_user ON tb_cart(user_id);

-- 收藏索引
CREATE INDEX idx_favorite_user ON tb_favorite(user_id);

-- 评价索引
CREATE INDEX idx_review_book ON tb_review(book_id);
CREATE INDEX idx_review_user ON tb_review(user_id);

-- 评价点赞索引
CREATE INDEX idx_review_like_review ON tb_review_like(review_id);
CREATE INDEX idx_review_like_user ON tb_review_like(user_id);

-- ============================================
-- 初始化数据
-- ============================================

-- 插入管理员账户
INSERT INTO tb_user (username, password, nickname, is_admin) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1);  -- 密码: 123456 (MD5)

-- 插入测试用户
INSERT INTO tb_user (username, password, nickname, email) VALUES 
('test', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 'test@example.com');

-- 插入一级分类
INSERT INTO tb_category (category_id, category_name, category_name_en, parent_id, category_level, sort_order) VALUES
(1, '计算机与互联网', 'Computer & Internet', 0, 1, 100),
(2, '文学小说', 'Literature & Fiction', 0, 1, 99),
(3, '经济管理', 'Business & Economics', 0, 1, 98),
(4, '教育学习', 'Education & Learning', 0, 1, 97),
(5, '生活健康', 'Health & Lifestyle', 0, 1, 96);

-- 插入二级分类（计算机）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('Java', 'Java', 1, 2, 100),
('Python', 'Python', 1, 2, 99),
('前端开发', 'Frontend Development', 1, 2, 98),
('算法与数据结构', 'Algorithms & Data Structures', 1, 2, 97);

-- 插入二级分类（文学）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('科幻小说', 'Science Fiction', 2, 2, 100),
('悬疑推理', 'Mystery & Thriller', 2, 2, 99),
('文学经典', 'Literary Classics', 2, 2, 98);

-- 插入示例电子书（中文）- 元数据
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro, 
                      category_id, cover_img, page_count, price, language, rating, rating_count) VALUES
('Java 编程思想', 'Bruce Eckel', '9787111213826', '机械工业出版社', '2007-06-01',
 'Java程序员必读的经典著作，深入浅出讲解Java语言的各个方面。',
 6, 'covers/1.jpg', 880, 8800, 'zh-CN', 4.8, 1250),

('三体', '刘慈欣', '9787536692930', '重庆出版社', '2008-01-01',
 '中国科幻文学的里程碑之作，雨果奖获奖作品。',
 10, 'covers/2.jpg', 456, 1800, 'zh-CN', 4.9, 25680),

('算法导论', 'Thomas H. Cormen', '9787111407010', '机械工业出版社', '2012-12-01',
 '算法领域的经典之作，被誉为算法圣经。',
 9, 'covers/3.jpg', 1312, 10800, 'zh-CN', 4.7, 1580);

-- 插入示例电子书（英文）- 元数据
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro,
                      category_id, cover_img, page_count, price, language, rating, rating_count) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'Prentice Hall', '2008-08-01',
 'A handbook of agile software craftsmanship that teaches the principles of writing clean code.',
 6, 'covers/4.jpg', 464, 4900, 'en-US', 4.7, 3420),

('The Pragmatic Programmer', 'David Thomas', '9780201616224', 'Addison-Wesley', '1999-10-20',
 'Your journey to mastery in software development starts here.',
 6, 'covers/5.jpg', 352, 3900, 'en-US', 4.8, 2890);

-- 插入电子书文件（多格式支持）
-- Java 编程思想 - 提供 PDF、EPUB、MOBI 三种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(1, 'PDF', 'books/pdf/1.pdf', 52428800),
(1, 'EPUB', 'books/epub/1.epub', 15728640),
(1, 'MOBI', 'books/mobi/1.mobi', 18874368);

-- 三体 - 提供 EPUB、MOBI、AZW3 三种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(2, 'EPUB', 'books/epub/2.epub', 3145728),
(2, 'MOBI', 'books/mobi/2.mobi', 4194304),
(2, 'AZW3', 'books/azw3/2.azw3', 3932160);

-- 算法导论 - 提供 PDF 格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(3, 'PDF', 'books/pdf/3.pdf', 65536000);

-- Clean Code - 提供 PDF、EPUB 两种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(4, 'PDF', 'books/pdf/4.pdf', 15728640),
(4, 'EPUB', 'books/epub/4.epub', 8388608);

-- The Pragmatic Programmer - 提供 PDF、MOBI 两种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(5, 'PDF', 'books/pdf/5.pdf', 12582912),
(5, 'MOBI', 'books/mobi/5.mobi', 10485760);


-- ============================================
-- 书单功能扩展表（新增）
-- ============================================

-- 9. 书单表
CREATE TABLE IF NOT EXISTS tb_booklist (
    list_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_name VARCHAR(100) NOT NULL,
    description TEXT,
    cover_img VARCHAR(200),
    is_public TINYINT DEFAULT 1,  -- 1-公开 0-私密
    book_count INTEGER DEFAULT 0,
    follow_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

-- 10. 书单-书籍关联表
CREATE TABLE IF NOT EXISTS tb_booklist_item (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    list_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    sort_order INTEGER DEFAULT 0,
    note TEXT,  -- 推荐理由/笔记
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(list_id, book_id)  -- 防止重复添加
);

-- 11. 书单关注表
CREATE TABLE IF NOT EXISTS tb_booklist_follow (
    follow_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    UNIQUE(user_id, list_id)  -- 防止重复关注
);

-- 书单表索引
CREATE INDEX idx_booklist_user ON tb_booklist(user_id);
CREATE INDEX idx_booklist_public ON tb_booklist(is_public);
CREATE INDEX idx_booklist_follow_count ON tb_booklist(follow_count DESC);

-- 书单项索引
CREATE INDEX idx_booklist_item_list ON tb_booklist_item(list_id);
CREATE INDEX idx_booklist_item_book ON tb_booklist_item(book_id);

-- 书单关注索引
CREATE INDEX idx_booklist_follow_user ON tb_booklist_follow(user_id);
CREATE INDEX idx_booklist_follow_list ON tb_booklist_follow(list_id);

-- ============================================
-- 触发器：自动更新书单统计数据
-- ============================================

-- 添加书籍时增加计数
CREATE TRIGGER IF NOT EXISTS trg_booklist_item_insert
AFTER INSERT ON tb_booklist_item
BEGIN
    UPDATE tb_booklist 
    SET book_count = book_count + 1,
        update_time = CURRENT_TIMESTAMP
    WHERE list_id = NEW.list_id;
END;

-- 删除书籍时减少计数
CREATE TRIGGER IF NOT EXISTS trg_booklist_item_delete
AFTER DELETE ON tb_booklist_item
BEGIN
    UPDATE tb_booklist 
    SET book_count = book_count - 1,
        update_time = CURRENT_TIMESTAMP
    WHERE list_id = OLD.list_id;
END;

-- 关注时增加计数
CREATE TRIGGER IF NOT EXISTS trg_booklist_follow_insert
AFTER INSERT ON tb_booklist_follow
BEGIN
    UPDATE tb_booklist 
    SET follow_count = follow_count + 1
    WHERE list_id = NEW.list_id;
END;

-- 取消关注时减少计数
CREATE TRIGGER IF NOT EXISTS trg_booklist_follow_delete
AFTER DELETE ON tb_booklist_follow
BEGIN
    UPDATE tb_booklist 
    SET follow_count = follow_count - 1
    WHERE list_id = OLD.list_id;
END;

-- ============================================
-- 书单示例数据
-- ============================================

-- 示例书单1：Java学习路线
INSERT INTO tb_booklist (user_id, list_name, description, is_public) VALUES
(1, 'Java学习路线', '从入门到精通的Java学习书单，适合初学者系统学习', 1);

-- 示例书单2：科幻小说推荐
INSERT INTO tb_booklist (user_id, list_name, description, is_public) VALUES
(1, '必读科幻小说', '经典科幻小说推荐，开启你的科幻之旅', 1);

-- 示例书单3：私密书单
INSERT INTO tb_booklist (user_id, list_name, description, is_public) VALUES
(2, '我的阅读计划', '2024年阅读计划', 0);

-- 添加书籍到书单
INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(1, 1, 1, '入门必读，讲解深入浅出'),
(1, 3, 2, '进阶学习，算法基础');

INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(2, 2, 1, '中国科幻的巅峰之作');

-- 示例关注
INSERT INTO tb_booklist_follow (user_id, list_id) VALUES
(2, 1);  -- 用户2关注了书单1


-- ============================================
-- 用户兴趣标签功能（新增）
-- ============================================

-- 12. 用户兴趣表
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

-- 示例：用户1选择了计算机和文学分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(1, 1),  -- 计算机与互联网
(1, 2);  -- 文学小说

-- 示例：用户2选择了计算机分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(2, 1);  -- 计算机与互联网

-- ============================================
-- 在线阅读功能（新增）
-- ============================================

-- 13. 阅读进度表（简化版）
CREATE TABLE IF NOT EXISTS tb_reading_progress (
    progress_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,  -- PDF/EPUB（用户阅读的格式）
    last_position TEXT,  -- 最后阅读位置（JSON格式）
    last_read_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE,
    UNIQUE(user_id, book_id, file_format)  -- 一个用户对一本书的一种格式只有一条记录
);

CREATE INDEX idx_reading_progress_user ON tb_reading_progress(user_id);
CREATE INDEX idx_reading_progress_last_read ON tb_reading_progress(last_read_time DESC);

-- 示例阅读进度数据
-- 用户1正在阅读《Java 编程思想》PDF版本，读到第156页
INSERT INTO tb_reading_progress (user_id, book_id, file_format, last_position, last_read_time) VALUES
(1, 1, 'PDF', '{"page":156,"scrollTop":320}', '2024-01-15 10:30:00');

-- 用户2正在阅读《三体》EPUB版本，读到特定位置
INSERT INTO tb_reading_progress (user_id, book_id, file_format, last_position, last_read_time) VALUES
(2, 2, 'EPUB', '{"cfi":"epubcfi(/6/14[chap07]!/4/2/1:0)","percent":45.0}', '2024-01-14 20:15:00');


-- ============================================
-- 下载管理功能（新增）
-- ============================================

-- 14. 下载记录表
CREATE TABLE IF NOT EXISTS tb_download_record (
    record_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,  -- 下载的文件格式
    download_url VARCHAR(500),  -- 临时下载链接
    expire_time DATETIME,  -- 链接过期时间
    download_count INTEGER DEFAULT 0,  -- 下载次数
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE
);

CREATE INDEX idx_download_record_user ON tb_download_record(user_id);
CREATE INDEX idx_download_record_book ON tb_download_record(book_id);
CREATE INDEX idx_download_record_expire ON tb_download_record(expire_time);

-- 示例下载记录
-- 用户1下载了《Java 编程思想》PDF版本
INSERT INTO tb_download_record (user_id, book_id, file_format, download_count) VALUES
(1, 1, 'PDF', 2);

-- 用户2下载了《三体》EPUB版本
INSERT INTO tb_download_record (user_id, book_id, file_format, download_count) VALUES
(2, 2, 'EPUB', 1);
