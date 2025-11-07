-- ============================================
-- IntelliBook-Mall 开发测试数据脚本
-- 用途：开发和测试环境初始化数据
-- 说明：包含必要的基础数据和测试数据
-- ============================================

-- ============================================
-- 1. 用户数据（必需）
-- ============================================

-- 管理员账户（密码: admin123，MD5加密）
INSERT INTO tb_user (username, password, nickname, email, is_admin) VALUES 
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 'admin@intellibook.com', 1);

-- 测试用户账户（密码: 123456，MD5加密）
INSERT INTO tb_user (username, password, nickname, email) VALUES 
('testuser', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 'test@example.com'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'zhangsan@example.com'),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'lisi@example.com');

-- ============================================
-- 2. 电子书分类数据（必需）
-- ============================================

-- 一级分类
INSERT INTO tb_category (category_id, category_name, category_name_en, parent_id, category_level, sort_order) VALUES
(1, '计算机与互联网', 'Computer & Internet', 0, 1, 100),
(2, '文学小说', 'Literature & Fiction', 0, 1, 99),
(3, '经济管理', 'Business & Economics', 0, 1, 98),
(4, '教育学习', 'Education & Learning', 0, 1, 97),
(5, '生活健康', 'Health & Lifestyle', 0, 1, 96);

-- 二级分类（计算机）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('Java', 'Java', 1, 2, 100),
('Python', 'Python', 1, 2, 99),
('前端开发', 'Frontend Development', 1, 2, 98),
('算法与数据结构', 'Algorithms & Data Structures', 1, 2, 97),
('数据库', 'Database', 1, 2, 96);

-- 二级分类（文学）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('科幻小说', 'Science Fiction', 2, 2, 100),
('悬疑推理', 'Mystery & Thriller', 2, 2, 99),
('文学经典', 'Literary Classics', 2, 2, 98),
('现代文学', 'Modern Literature', 2, 2, 97);

-- 二级分类（经济管理）
INSERT INTO tb_category (category_name, category_name_en, parent_id, category_level, sort_order) VALUES
('企业管理', 'Business Management', 3, 2, 100),
('市场营销', 'Marketing', 3, 2, 99),
('金融投资', 'Finance & Investment', 3, 2, 98);

-- ============================================
-- 3. 示例电子书数据（可选，用于开发测试）
-- ============================================

-- 中文技术书籍
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro, 
                      category_id, cover_img, page_count, price, language, tags, rating, rating_count) VALUES
('Java 编程思想（第4版）', 'Bruce Eckel', '9787111213826', '机械工业出版社', '2007-06-01',
 'Java程序员必读的经典著作，深入浅出讲解Java语言的各个方面，涵盖面向对象编程、异常处理、集合框架、并发编程等核心内容。',
 6, 'covers/java-thinking.jpg', 880, 8800, 'zh-CN', 'Java,编程,面向对象', 4.8, 1250),

('算法导论（第3版）', 'Thomas H. Cormen', '9787111407010', '机械工业出版社', '2012-12-01',
 '算法领域的经典之作，被誉为算法圣经。全面介绍了计算机算法的理论基础和实际应用。',
 9, 'covers/algorithm-intro.jpg', 1312, 10800, 'zh-CN', '算法,数据结构,计算机科学', 4.7, 1580),

('深入理解计算机系统（第3版）', 'Randal E. Bryant', '9787111544937', '机械工业出版社', '2016-11-01',
 '从程序员的视角深入理解计算机系统，涵盖数据表示、汇编语言、处理器体系结构、存储器层次结构等内容。',
 1, 'covers/csapp.jpg', 736, 13900, 'zh-CN', '计算机系统,底层原理,C语言', 4.9, 2340);

-- 英文技术书籍
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro,
                      category_id, cover_img, page_count, price, language, tags, rating, rating_count) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'Prentice Hall', '2008-08-01',
 'A handbook of agile software craftsmanship that teaches the principles of writing clean, maintainable code.',
 6, 'covers/clean-code.jpg', 464, 4900, 'en-US', 'Clean Code,Best Practices,Software Engineering', 4.7, 3420),

('Design Patterns', 'Erich Gamma', '9780201633610', 'Addison-Wesley', '1994-10-31',
 'Elements of Reusable Object-Oriented Software. The classic book on design patterns.',
 6, 'covers/design-patterns.jpg', 395, 5900, 'en-US', 'Design Patterns,OOP,Software Architecture', 4.6, 2890);

-- 中文文学作品
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro,
                      category_id, cover_img, page_count, price, language, tags, rating, rating_count) VALUES
('三体', '刘慈欣', '9787536692930', '重庆出版社', '2008-01-01',
 '中国科幻文学的里程碑之作，雨果奖获奖作品。讲述了地球文明与三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。',
 11, 'covers/three-body.jpg', 456, 1800, 'zh-CN', '科幻,刘慈欣,雨果奖', 4.9, 25680),

('活着', '余华', '9787506365437', '作家出版社', '2012-08-01',
 '余华代表作，讲述了一个人和他命运之间的友情，这是最为感人的友情，因为他们互相感激，同时也互相仇恨。',
 14, 'covers/to-live.jpg', 191, 1200, 'zh-CN', '现代文学,余华,人生', 4.8, 18920);

-- ============================================
-- 4. 电子书文件数据（与上面的书籍对应）
-- ============================================

-- Java 编程思想 - 提供 PDF、EPUB 两种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(1, 'PDF', 'books/pdf/java-thinking.pdf', 52428800),
(1, 'EPUB', 'books/epub/java-thinking.epub', 15728640);

-- 算法导论 - 提供 PDF 格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(2, 'PDF', 'books/pdf/algorithm-intro.pdf', 65536000);

-- 深入理解计算机系统 - 提供 PDF 格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(3, 'PDF', 'books/pdf/csapp.pdf', 78643200);

-- Clean Code - 提供 PDF、EPUB 两种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(4, 'PDF', 'books/pdf/clean-code.pdf', 15728640),
(4, 'EPUB', 'books/epub/clean-code.epub', 8388608);

-- Design Patterns - 提供 PDF 格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(5, 'PDF', 'books/pdf/design-patterns.pdf', 12582912);

-- 三体 - 提供 EPUB、MOBI 两种格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(6, 'EPUB', 'books/epub/three-body.epub', 3145728),
(6, 'MOBI', 'books/mobi/three-body.mobi', 4194304);

-- 活着 - 提供 EPUB 格式
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
(7, 'EPUB', 'books/epub/to-live.epub', 2097152);

-- ============================================
-- 5. 用户兴趣数据（示例）
-- ============================================

-- 用户2（testuser）选择了计算机和文学分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(2, 1),  -- 计算机与互联网
(2, 2);  -- 文学小说

-- 用户3（zhangsan）选择了计算机分类
INSERT INTO tb_user_interest (user_id, category_id) VALUES
(3, 1),  -- 计算机与互联网
(3, 3);  -- 经济管理

-- ============================================
-- 6. 示例书单数据（可选）
-- ============================================

-- 管理员创建的公开书单
INSERT INTO tb_booklist (user_id, list_name, description, is_public) VALUES
(1, 'Java学习路线', '从入门到精通的Java学习书单，适合初学者系统学习', 1),
(1, '经典科幻小说推荐', '不容错过的科幻文学经典作品', 1);

-- 用户创建的私密书单
INSERT INTO tb_booklist (user_id, list_name, description, is_public) VALUES
(2, '我的2024阅读计划', '今年要读完的书籍清单', 0);

-- 书单项
INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(1, 1, 1, '入门必读，讲解深入浅出，适合Java初学者'),
(1, 2, 2, '进阶学习，掌握算法和数据结构基础');

INSERT INTO tb_booklist_item (list_id, book_id, sort_order, note) VALUES
(2, 6, 1, '中国科幻的巅峰之作，必读！');

-- 书单关注
INSERT INTO tb_booklist_follow (user_id, list_id) VALUES
(2, 1),  -- testuser 关注了 Java学习路线
(3, 1);  -- zhangsan 关注了 Java学习路线

-- ============================================
-- 7. 示例订单数据（可选，用于测试）
-- ============================================

-- 用户2的已完成订单
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status) VALUES
('202401150001', 2, 8800, 1, 1, '2024-01-15 10:30:00', 1);

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES
(1, 1, 'Java 编程思想（第4版）', 8800);

-- 用户3的已完成订单
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status) VALUES
('202401160001', 3, 1800, 1, 2, '2024-01-16 14:20:00', 1);

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES
(2, 6, '三体', 1800);

-- ============================================
-- 8. 示例评价数据（可选）
-- ============================================

-- 用户2对《Java 编程思想》的评价
INSERT INTO tb_review (user_id, book_id, rating, content) VALUES
(2, 1, 5, '非常经典的Java书籍，讲解深入浅出，适合有一定基础的开发者阅读。通过这本书对Java的理解更加深刻了。');

-- 用户3对《三体》的评价
INSERT INTO tb_review (user_id, book_id, rating, content) VALUES
(3, 6, 5, '震撼！刘慈欣的想象力太丰富了，黑暗森林法则让人细思极恐。强烈推荐！');

-- ============================================
-- 9. 示例收藏数据（可选）
-- ============================================

-- 用户2的收藏
INSERT INTO tb_favorite (user_id, book_id) VALUES
(2, 2),  -- 算法导论
(2, 3),  -- 深入理解计算机系统
(2, 6);  -- 三体

-- 用户3的收藏
INSERT INTO tb_favorite (user_id, book_id) VALUES
(3, 1),  -- Java 编程思想
(3, 4);  -- Clean Code

-- ============================================
-- 10. 示例阅读进度数据（可选）
-- ============================================

-- 用户2正在阅读《Java 编程思想》PDF版本
INSERT INTO tb_reading_progress (user_id, book_id, file_format, last_position, last_read_time) VALUES
(2, 1, 'PDF', '{"page":156,"scrollTop":320}', '2024-01-15 20:30:00');

-- 用户3正在阅读《三体》EPUB版本
INSERT INTO tb_reading_progress (user_id, book_id, file_format, last_position, last_read_time) VALUES
(3, 6, 'EPUB', '{"cfi":"epubcfi(/6/14[chap07]!/4/2/1:0)","percent":45.0}', '2024-01-16 21:15:00');

-- ============================================
-- 数据初始化完成
-- ============================================

-- 统计信息
SELECT '=== 数据初始化统计 ===' AS info;
SELECT '用户数量: ' || COUNT(*) AS info FROM tb_user;
SELECT '分类数量: ' || COUNT(*) AS info FROM tb_category;
SELECT '电子书数量: ' || COUNT(*) AS info FROM tb_ebook;
SELECT '电子书文件数量: ' || COUNT(*) AS info FROM tb_ebook_file;
SELECT '书单数量: ' || COUNT(*) AS info FROM tb_booklist;
SELECT '订单数量: ' || COUNT(*) AS info FROM tb_order;
SELECT '评价数量: ' || COUNT(*) AS info FROM tb_review;
SELECT '收藏数量: ' || COUNT(*) AS info FROM tb_favorite;
