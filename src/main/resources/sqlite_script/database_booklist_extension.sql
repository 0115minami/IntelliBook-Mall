-- ============================================
-- 书单功能扩展表设计
-- ============================================

-- 1. 书单表
CREATE TABLE IF NOT EXISTS tb_booklist (
    list_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_name VARCHAR(100) NOT NULL,
    description TEXT,
    cover_img VARCHAR(200),  -- 书单封面（可选，使用第一本书的封面）
    is_public TINYINT DEFAULT 1,  -- 1-公开 0-私密
    book_count INTEGER DEFAULT 0,  -- 书单中的书籍数量
    follow_count INTEGER DEFAULT 0,  -- 关注数
    view_count INTEGER DEFAULT 0,  -- 浏览次数
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id)
);

-- 2. 书单-书籍关联表
CREATE TABLE IF NOT EXISTS tb_booklist_item (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    list_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    sort_order INTEGER DEFAULT 0,  -- 排序值
    note TEXT,  -- 推荐理由/笔记
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES tb_ebook(book_id),
    UNIQUE(list_id, book_id)  -- 防止重复添加
);

-- 3. 书单关注表
CREATE TABLE IF NOT EXISTS tb_booklist_follow (
    follow_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    list_id INTEGER NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tb_user(user_id),
    FOREIGN KEY (list_id) REFERENCES tb_booklist(list_id) ON DELETE CASCADE,
    UNIQUE(user_id, list_id)  -- 防止重复关注
);

-- ============================================
-- 索引优化
-- ============================================

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
-- 触发器：自动更新书单的书籍数量
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
-- 初始化示例数据
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

-- 添加书籍到书单（假设已有 book_id 1, 2, 3）
INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(1, 1, 1, '入门必读，讲解深入浅出'),
(1, 3, 2, '进阶学习，算法基础');

INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(2, 2, 1, '中国科幻的巅峰之作');

-- 示例关注
INSERT INTO tb_booklist_follow (user_id, list_id) VALUES
(2, 1);  -- 用户2关注了书单1
