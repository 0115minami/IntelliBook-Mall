-- ============================================
-- IntelliBook-Mall 数据库表结构脚本（仅表结构）
-- 数据库类型：SQLite 3
-- 用途：生产环境初始化
-- ============================================

-- ============================================
-- 核心业务表
-- ============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS tb_user (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    is_admin TINYINT DEFAULT 0,
    locked_flag TINYINT DEFAULT 0,
    login_fail_count INTEGER DEFAULT 0,
    lock_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. 电子书分类表
CREATE TABLE IF NOT EXISTS tb_category (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name VARCHAR(50) NOT NULL,
    category_name_en VARCHAR(50),
    parent_id INTEGER DEFAULT 0,
    category_level TINYINT DEFAULT 1,
    sort_order INTEGER DEFAULT 0,
    FOREIGN KEY (parent_id) REFERENCES tb_category(category_id)
);

-- 3. 电子书表（核心表）
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
    price INTEGER DEFAULT 0,
    language VARCHAR(10) DEFAULT 'zh-CN',
    tags VARCHAR(500),
    rating REAL DEFAULT 0.0,
    rating_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,
    status TINYINT DEFAULT 1,
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id)
);

-- 4. 电子书文件表（支持多格式）
CREATE TABLE IF NOT EXISTS tb_ebook_file (
    file_id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size INTEGER,
    download_count INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE,
    UNIQUE(book_id, file_format)
);

-- 5. 订单表
CREATE TABLE IF NOT EXISTS tb_order (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no VARCHAR(20) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL,
    total_price INTEGER NOT NULL,
    pay_status TINYINT DEFAULT 0,
    pay_type TINYINT DEFAULT 0,
    pay_time DATETIME,
    order_status TINYINT DEFAULT 0,
    extra_info VARCHAR(500),
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

-- 6. 订单项表
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

-- 7. 购物车表
CREATE TABLE IF NOT EXISTS tb_cart (
    cart_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)
);

-- 8. 收藏表
CREATE TABLE IF NOT EXISTS tb_favorite (
    favorite_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(user_id, book_id)
);

-- 9. 评价表
CREATE TABLE IF NOT EXISTS tb_review (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    rating TINYINT NOT NULL,
    content TEXT,
    like_count INTEGER DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id)
);

-- 10. 评价点赞表
CREATE TABLE IF NOT EXISTS tb_review_like (
    like_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    review_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (review_id) REFERENCES tb_review(review_id) ON DELETE CASCADE,
    UNIQUE(user_id, review_id)
);

-- ============================================
-- 扩展功能表
-- ============================================

-- 11. 书单表
CREATE TABLE IF NOT EXISTS tb_booklist (
    list_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_name VARCHAR(100) NOT NULL,
    description TEXT,
    cover_img VARCHAR(200),
    is_public TINYINT DEFAULT 1,
    book_count INTEGER DEFAULT 0,
    follow_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

-- 12. 书单-书籍关联表
CREATE TABLE IF NOT EXISTS tb_booklist_item (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    list_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    sort_order INTEGER DEFAULT 0,
    note TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(list_id, book_id)
);

-- 13. 书单关注表
CREATE TABLE IF NOT EXISTS tb_booklist_follow (
    follow_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    UNIQUE(user_id, list_id)
);

-- 14. 用户兴趣表
CREATE TABLE IF NOT EXISTS tb_user_interest (
    interest_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (category_id) REFERENCES tb_category(category_id),
    UNIQUE(user_id, category_id)
);

-- 15. 阅读进度表
CREATE TABLE IF NOT EXISTS tb_reading_progress (
    progress_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,
    last_position TEXT,
    last_read_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE,
    UNIQUE(user_id, book_id, file_format)
);

-- 16. 下载记录表
CREATE TABLE IF NOT EXISTS tb_download_record (
    record_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    file_format VARCHAR(10) NOT NULL,
    download_url VARCHAR(500),
    expire_time DATETIME,
    download_count INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id) ON DELETE CASCADE
);

-- ============================================
-- 索引创建
-- ============================================

-- 电子书表索引
CREATE INDEX IF NOT EXISTS idx_ebook_category ON tb_ebook(category_id);
CREATE INDEX IF NOT EXISTS idx_ebook_language ON tb_ebook(language);
CREATE INDEX IF NOT EXISTS idx_ebook_author ON tb_ebook(author);
CREATE INDEX IF NOT EXISTS idx_ebook_status ON tb_ebook(status);
CREATE INDEX IF NOT EXISTS idx_ebook_isbn ON tb_ebook(isbn);

-- 电子书文件表索引
CREATE INDEX IF NOT EXISTS idx_ebook_file_book ON tb_ebook_file(book_id);
CREATE INDEX IF NOT EXISTS idx_ebook_file_format ON tb_ebook_file(file_format);

-- 订单表索引
CREATE INDEX IF NOT EXISTS idx_order_user ON tb_order(user_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON tb_order(order_status);
CREATE INDEX IF NOT EXISTS idx_order_no ON tb_order(order_no);

-- 购物车索引
CREATE INDEX IF NOT EXISTS idx_cart_user ON tb_cart(user_id);

-- 收藏索引
CREATE INDEX IF NOT EXISTS idx_favorite_user ON tb_favorite(user_id);
CREATE INDEX IF NOT EXISTS idx_favorite_book ON tb_favorite(book_id);

-- 评价索引
CREATE INDEX IF NOT EXISTS idx_review_book ON tb_review(book_id);
CREATE INDEX IF NOT EXISTS idx_review_user ON tb_review(user_id);

-- 评价点赞索引
CREATE INDEX IF NOT EXISTS idx_review_like_review ON tb_review_like(review_id);
CREATE INDEX IF NOT EXISTS idx_review_like_user ON tb_review_like(user_id);

-- 书单表索引
CREATE INDEX IF NOT EXISTS idx_booklist_user ON tb_booklist(user_id);
CREATE INDEX IF NOT EXISTS idx_booklist_public ON tb_booklist(is_public);
CREATE INDEX IF NOT EXISTS idx_booklist_follow_count ON tb_booklist(follow_count DESC);

-- 书单项索引
CREATE INDEX IF NOT EXISTS idx_booklist_item_list ON tb_booklist_item(list_id);
CREATE INDEX IF NOT EXISTS idx_booklist_item_book ON tb_booklist_item(book_id);

-- 书单关注索引
CREATE INDEX IF NOT EXISTS idx_booklist_follow_user ON tb_booklist_follow(user_id);
CREATE INDEX IF NOT EXISTS idx_booklist_follow_list ON tb_booklist_follow(list_id);

-- 用户兴趣索引
CREATE INDEX IF NOT EXISTS idx_user_interest_user ON tb_user_interest(user_id);
CREATE INDEX IF NOT EXISTS idx_user_interest_category ON tb_user_interest(category_id);

-- 阅读进度索引
CREATE INDEX IF NOT EXISTS idx_reading_progress_user ON tb_reading_progress(user_id);
CREATE INDEX IF NOT EXISTS idx_reading_progress_last_read ON tb_reading_progress(last_read_time DESC);

-- 下载记录索引
CREATE INDEX IF NOT EXISTS idx_download_record_user ON tb_download_record(user_id);
CREATE INDEX IF NOT EXISTS idx_download_record_book ON tb_download_record(book_id);
CREATE INDEX IF NOT EXISTS idx_download_record_expire ON tb_download_record(expire_time);

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
-- 表结构创建完成
-- ============================================
