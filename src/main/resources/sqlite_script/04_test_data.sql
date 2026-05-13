-- ============================================
-- IntelliBook-Mall 测试数据脚本
-- 用途：开发和测试环境数据初始化
-- 说明：包含普通用户、购物车、收藏、订单、评论等测试数据
-- 注意：需要先执行 01_schema_only.sql, 03_minimal_required_data.sql, 02_ebook_data.sql
-- ============================================

-- ============================================
-- 1. 普通用户数据
-- ============================================

-- 测试用户账户
-- 密码统一为: 123456 (MD5: e10adc3949ba59abbe56e057f20f883e)
INSERT INTO tb_user (username, password, nickname, email, is_admin) VALUES 
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'zhangsan@example.com', 0),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'lisi@example.com', 0),
('wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', 'wangwu@example.com', 0),
('zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 'zhaoliu@example.com', 0),
('sunqi', 'e10adc3949ba59abbe56e057f20f883e', '孙七', 'sunqi@example.com', 0),
('alice', 'e10adc3949ba59abbe56e057f20f883e', 'Alice Wang', 'alice@example.com', 0),
('bob', 'e10adc3949ba59abbe56e057f20f883e', 'Bob Chen', 'bob@example.com', 0),
('carol', 'e10adc3949ba59abbe56e057f20f883e', 'Carol Liu', 'carol@example.com', 0);

-- ============================================
-- 2. 用户兴趣数据
-- ============================================

-- 张三的兴趣：计算机、人工智能、Python
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(2, 1),   -- 计算机与互联网
(2, 10),  -- Python
(2, 17);  -- 人工智能

-- 李四的兴趣：文学、现代文学、悬疑推理
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(3, 2),   -- 文学小说
(3, 20),  -- 悬疑推理
(3, 23);  -- 现代文学

-- 王五的兴趣：经济管理、企业管理
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(4, 3),   -- 经济管理
(4, 25),  -- 企业管理
(4, 29);  -- 经济学

-- 赵六的兴趣：教育学习、外语学习
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(5, 4),   -- 教育学习
(5, 30),  -- 外语学习
(5, 31);  -- 职业技能

-- 孙七的兴趣：生活健康、美食烹饪
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(6, 5),   -- 生活健康
(6, 34);  -- 美食烹饪

-- Alice的兴趣：计算机、前端开发
INSERT INTO tb_user_interest (user_id, category_id) VALUES 
(7, 1),   -- 计算机与互联网
(7, 12);  -- 前端开发

-- ============================================
-- 3. 购物车数据
-- ============================================

-- 张三的购物车（3本书）
INSERT INTO tb_cart (user_id, book_id, create_time) VALUES 
(2, 1, datetime('now', '-2 days')),   -- 深度学习入门
(2, 4, datetime('now', '-1 day')),    -- 国富论
(2, 9, datetime('now', '-3 hours'));  -- Git for Teams

-- 李四的购物车（2本书）
INSERT INTO tb_cart (user_id, book_id, create_time) VALUES 
(3, 2, datetime('now', '-5 days')),   -- 红玫瑰与白玫瑰
(3, 10, datetime('now', '-1 day'));   -- 幽灵塔

-- 王五的购物车（1本书）
INSERT INTO tb_cart (user_id, book_id, create_time) VALUES 
(4, 5, datetime('now', '-2 hours'));  -- 心：稻盛和夫的一生嘱托

-- 赵六的购物车（2本书）
INSERT INTO tb_cart (user_id, book_id, create_time) VALUES 
(5, 3, datetime('now', '-1 day')),    -- TOEFL iBT 备考指南
(5, 11, datetime('now', '-6 hours')); -- 小说写作：叙事技巧指南

-- Alice的购物车（1本书）
INSERT INTO tb_cart (user_id, book_id, create_time) VALUES 
(7, 9, datetime('now', '-4 hours'));  -- Git for Teams

-- ============================================
-- 4. 收藏数据
-- ============================================

-- 张三的收藏（5本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(2, 1, datetime('now', '-30 days')),  -- 深度学习入门
(2, 9, datetime('now', '-15 days')),  -- Git for Teams
(2, 4, datetime('now', '-10 days')),  -- 国富论
(2, 11, datetime('now', '-5 days')),  -- 小说写作
(2, 6, datetime('now', '-2 days'));   -- 飞鸟集

-- 李四的收藏（4本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(3, 2, datetime('now', '-25 days')),  -- 红玫瑰与白玫瑰
(3, 6, datetime('now', '-20 days')),  -- 飞鸟集
(3, 10, datetime('now', '-12 days')), -- 幽灵塔
(3, 12, datetime('now', '-3 days'));  -- 东京梦华录

-- 王五的收藏（3本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(4, 4, datetime('now', '-40 days')),  -- 国富论
(4, 5, datetime('now', '-18 days')),  -- 心：稻盛和夫的一生嘱托
(4, 11, datetime('now', '-7 days'));  -- 小说写作

-- 赵六的收藏（3本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(5, 3, datetime('now', '-22 days')),  -- TOEFL iBT 备考指南
(5, 11, datetime('now', '-14 days')), -- 小说写作
(5, 1, datetime('now', '-8 days'));   -- 深度学习入门

-- 孙七的收藏（2本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(6, 7, datetime('now', '-16 days')),  -- 中国野菜图鉴
(6, 8, datetime('now', '-9 days'));   -- 茹素之乐

-- Alice的收藏（3本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(7, 9, datetime('now', '-28 days')),  -- Git for Teams
(7, 1, datetime('now', '-11 days')),  -- 深度学习入门
(7, 11, datetime('now', '-4 days'));  -- 小说写作

-- Bob的收藏（2本书）
INSERT INTO tb_favorite (user_id, book_id, create_time) VALUES 
(8, 4, datetime('now', '-19 days')),  -- 国富论
(8, 6, datetime('now', '-6 days'));   -- 飞鸟集

-- ============================================
-- 5. 订单数据
-- ============================================

-- 订单1：张三购买了2本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130001', 2, 8300, 1, 1, datetime('now', '-20 days'), 1, datetime('now', '-20 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(1, 1, '深度学习入门:基于 Python 的理论与实现', 5900),
(1, 6, '飞鸟集', 2400);

-- 订单2：李四购买了1本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130002', 3, 2800, 1, 2, datetime('now', '-18 days'), 1, datetime('now', '-18 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(2, 2, '红玫瑰与白玫瑰', 2800);

-- 订单3：王五购买了3本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130003', 4, 16900, 1, 1, datetime('now', '-15 days'), 1, datetime('now', '-15 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(3, 4, '国富论', 6800),
(3, 5, '心：稻盛和夫的一生嘱托', 4900),
(3, 11, '小说写作：叙事技巧指南', 5200);

-- 订单4：赵六购买了1本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130004', 5, 3900, 1, 1, datetime('now', '-12 days'), 1, datetime('now', '-12 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(4, 3, 'TOEFL iBT Preparation Book: Test Prep for Reading, Listening, Speaking, & Writing', 3900);

-- 订单5：孙七购买了2本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130005', 6, 9600, 1, 2, datetime('now', '-10 days'), 1, datetime('now', '-10 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(5, 7, '中国野菜图鉴 (中国之美自然生态图鉴)', 5800),
(5, 8, '茹素之乐: 美味素食菜谱100例', 3800);

-- 订单6：Alice购买了1本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130006', 7, 4500, 1, 1, datetime('now', '-8 days'), 1, datetime('now', '-8 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(6, 9, 'Git for Teams: A User-Centered Approach to Creating Efficient Workflows in Git', 4500);

-- 订单7：张三购买了1本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130007', 2, 3200, 1, 2, datetime('now', '-5 days'), 1, datetime('now', '-5 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(7, 10, '幽灵塔', 3200);

-- 订单8：Bob购买了2本书（已支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, pay_time, order_status, create_time) VALUES 
('ORD202605130008', 8, 9200, 1, 1, datetime('now', '-3 days'), 1, datetime('now', '-3 days'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(8, 4, '国富论', 6800),
(8, 6, '飞鸟集', 2400);

-- 订单9：李四购买了1本书（待支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, order_status, create_time) VALUES 
('ORD202605130009', 3, 3200, 0, 0, 0, datetime('now', '-1 day'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(9, 10, '幽灵塔', 3200);

-- 订单10：Carol购买了1本书（待支付）
INSERT INTO tb_order (order_no, user_id, total_price, pay_status, pay_type, order_status, create_time) VALUES 
('ORD202605130010', 9, 3600, 0, 0, 0, datetime('now', '-2 hours'));

INSERT INTO tb_order_item (order_id, book_id, book_title, price) VALUES 
(10, 12, '东京梦华录', 3600);

-- ============================================
-- 6. 评论数据
-- ============================================

-- 书籍1（深度学习入门）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(2, 1, 5, '非常适合深度学习入门的书籍！作者用Python实现了各种神经网络，代码清晰易懂，理论讲解也很到位。强烈推荐给想学习深度学习的朋友！', 45, datetime('now', '-19 days')),
(7, 1, 5, '这本书真的很棒！从零开始构建神经网络，让我对深度学习有了更深入的理解。配套代码也很完整，跟着敲一遍收获很大。', 32, datetime('now', '-10 days')),
(5, 1, 4, '内容不错，适合有一定Python基础的读者。不过有些数学推导部分可能需要额外查阅资料才能完全理解。', 18, datetime('now', '-7 days'));

-- 书籍2（红玫瑰与白玫瑰）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(3, 2, 5, '张爱玲的文字总是那么细腻动人。这本书深刻地描绘了人性的复杂和爱情的矛盾，每次读都有新的感悟。', 67, datetime('now', '-17 days')),
(4, 2, 5, '经典之作！"也许每一个男子全都有过这样的两个女人，至少两个。"这句话太经典了。', 52, datetime('now', '-12 days'));

-- 书籍3（TOEFL iBT 备考指南）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(5, 3, 4, 'Good preparation book for TOEFL. The practice tests are helpful and the strategies are practical. Recommended for test takers.', 23, datetime('now', '-11 days'));

-- 书籍4（国富论）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(4, 4, 5, '经济学的圣经！虽然是两百多年前的著作，但其中的很多观点在今天依然适用。理解现代经济学必读的经典。', 89, datetime('now', '-14 days')),
(8, 4, 5, '亚当·斯密的思想深刻而系统，这本书奠定了现代经济学的基础。虽然篇幅较长，但值得耐心阅读。', 56, datetime('now', '-2 days'));

-- 书籍5（心：稻盛和夫的一生嘱托）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(4, 5, 5, '稻盛和夫先生的人生智慧和经营哲学令人敬佩。这本书不仅适合企业管理者，也适合每一个追求人生意义的人阅读。', 78, datetime('now', '-13 days'));

-- 书籍6（飞鸟集）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(2, 6, 5, '泰戈尔的诗歌如同清泉，洗涤心灵。每一句都充满哲理和美感，适合在安静的时候慢慢品读。', 94, datetime('now', '-18 days')),
(3, 6, 5, '非常喜欢泰戈尔的诗！《飞鸟集》、《吉檀迦利》和《园丁集》都收录在内，性价比很高。', 71, datetime('now', '-15 days')),
(8, 6, 5, '诗意盎然，意境深远。泰戈尔的文字总能触动人心最柔软的地方。', 43, datetime('now', '-5 days'));

-- 书籍7（中国野菜图鉴）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(6, 7, 4, '图文并茂，介绍详细。对于喜欢野外采摘和了解野菜的朋友来说是一本很实用的工具书。', 28, datetime('now', '-9 days'));

-- 书籍8（茹素之乐）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(6, 8, 4, '素食菜谱很丰富，做法也不复杂。照着书做了几道菜，味道都不错。推荐给想尝试素食的朋友。', 35, datetime('now', '-8 days'));

-- 书籍9（Git for Teams）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(7, 9, 4, 'Great book for understanding Git workflows in a team environment. The user-centered approach makes it easy to understand complex concepts.', 41, datetime('now', '-7 days'));

-- 书籍10（幽灵塔）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(3, 10, 5, '江户川乱步不愧是日本推理小说之父！情节紧凑，悬念迭起，一口气读完根本停不下来。', 62, datetime('now', '-4 days')),
(2, 10, 4, '推理小说爱好者必读！虽然有些情节在今天看来可能有点老套，但在当时绝对是开创性的作品。', 29, datetime('now', '-4 days'));

-- 书籍11（小说写作：叙事技巧指南）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(4, 11, 5, '对于想要提升写作能力的人来说，这是一本非常实用的指南。从叙事技巧到人物塑造，讲解得非常系统和深入。', 54, datetime('now', '-6 days')),
(5, 11, 5, '作为一名写作爱好者，这本书给了我很多启发。书中的案例分析特别有帮助，让我明白了好故事是如何构建的。', 38, datetime('now', '-3 days'));

-- 书籍12（东京梦华录）的评论
INSERT INTO tb_review (user_id, book_id, rating, content, like_count, create_time) VALUES 
(3, 12, 5, '通过这本书可以了解北宋都城的繁华景象和市井生活，对于喜欢历史的读者来说是一本很好的读物。', 47, datetime('now', '-2 days'));

-- ============================================
-- 7. 评论点赞数据
-- ============================================

-- 为热门评论添加点赞记录（部分示例）
-- 书籍6（飞鸟集）第一条评论的点赞
INSERT INTO tb_review_like (user_id, review_id, create_time) VALUES 
(3, 7, datetime('now', '-17 days')),
(4, 7, datetime('now', '-16 days')),
(5, 7, datetime('now', '-15 days')),
(6, 7, datetime('now', '-14 days')),
(7, 7, datetime('now', '-13 days'));

-- 书籍4（国富论）第一条评论的点赞
INSERT INTO tb_review_like (user_id, review_id, create_time) VALUES 
(2, 5, datetime('now', '-13 days')),
(3, 5, datetime('now', '-12 days')),
(5, 5, datetime('now', '-11 days')),
(7, 5, datetime('now', '-10 days'));

-- 书籍1（深度学习入门）第一条评论的点赞
INSERT INTO tb_review_like (user_id, review_id, create_time) VALUES 
(3, 1, datetime('now', '-18 days')),
(4, 1, datetime('now', '-17 days')),
(5, 1, datetime('now', '-16 days'));

-- ============================================
-- 8. 更新电子书统计数据
-- ============================================

-- 更新评分和评分人数
UPDATE tb_ebook SET rating = 4.7, rating_count = 3 WHERE book_id = 1;  -- 深度学习入门
UPDATE tb_ebook SET rating = 5.0, rating_count = 2 WHERE book_id = 2;  -- 红玫瑰与白玫瑰
UPDATE tb_ebook SET rating = 4.0, rating_count = 1 WHERE book_id = 3;  -- TOEFL iBT
UPDATE tb_ebook SET rating = 5.0, rating_count = 2 WHERE book_id = 4;  -- 国富论
UPDATE tb_ebook SET rating = 5.0, rating_count = 1 WHERE book_id = 5;  -- 心
UPDATE tb_ebook SET rating = 5.0, rating_count = 3 WHERE book_id = 6;  -- 飞鸟集
UPDATE tb_ebook SET rating = 4.0, rating_count = 1 WHERE book_id = 7;  -- 中国野菜图鉴
UPDATE tb_ebook SET rating = 4.0, rating_count = 1 WHERE book_id = 8;  -- 茹素之乐
UPDATE tb_ebook SET rating = 4.0, rating_count = 1 WHERE book_id = 9;  -- Git for Teams
UPDATE tb_ebook SET rating = 4.5, rating_count = 2 WHERE book_id = 10; -- 幽灵塔
UPDATE tb_ebook SET rating = 5.0, rating_count = 2 WHERE book_id = 11; -- 小说写作
UPDATE tb_ebook SET rating = 5.0, rating_count = 1 WHERE book_id = 12; -- 东京梦华录

-- 更新浏览量（模拟用户浏览）
UPDATE tb_ebook SET view_count = 1250 WHERE book_id = 1;
UPDATE tb_ebook SET view_count = 980 WHERE book_id = 2;
UPDATE tb_ebook SET view_count = 567 WHERE book_id = 3;
UPDATE tb_ebook SET view_count = 1456 WHERE book_id = 4;
UPDATE tb_ebook SET view_count = 892 WHERE book_id = 5;
UPDATE tb_ebook SET view_count = 2134 WHERE book_id = 6;
UPDATE tb_ebook SET view_count = 432 WHERE book_id = 7;
UPDATE tb_ebook SET view_count = 389 WHERE book_id = 8;
UPDATE tb_ebook SET view_count = 678 WHERE book_id = 9;
UPDATE tb_ebook SET view_count = 845 WHERE book_id = 10;
UPDATE tb_ebook SET view_count = 723 WHERE book_id = 11;
UPDATE tb_ebook SET view_count = 534 WHERE book_id = 12;

-- 更新下载量（基于订单数据）
UPDATE tb_ebook SET download_count = 2 WHERE book_id = 1;  -- 张三、Alice
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 2;  -- 李四
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 3;  -- 赵六
UPDATE tb_ebook SET download_count = 2 WHERE book_id = 4;  -- 王五、Bob
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 5;  -- 王五
UPDATE tb_ebook SET download_count = 3 WHERE book_id = 6;  -- 张三、Bob
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 7;  -- 孙七
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 8;  -- 孙七
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 9;  -- Alice
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 10; -- 张三
UPDATE tb_ebook SET download_count = 1 WHERE book_id = 11; -- 王五

-- ============================================
-- 数据插入完成提示
-- ============================================

SELECT '=== 测试数据初始化完成 ===' AS info;
SELECT '普通用户数量: ' || COUNT(*) AS info FROM tb_user WHERE is_admin = 0;
SELECT '购物车记录数: ' || COUNT(*) AS info FROM tb_cart WHERE is_deleted = 0;
SELECT '收藏记录数: ' || COUNT(*) AS info FROM tb_favorite;
SELECT '订单数量: ' || COUNT(*) AS info FROM tb_order;
SELECT '已支付订单: ' || COUNT(*) AS info FROM tb_order WHERE pay_status = 1;
SELECT '待支付订单: ' || COUNT(*) AS info FROM tb_order WHERE pay_status = 0;
SELECT '评论数量: ' || COUNT(*) AS info FROM tb_review WHERE is_deleted = 0;
SELECT '评论点赞数: ' || COUNT(*) AS info FROM tb_review_like;
SELECT '用户兴趣记录: ' || COUNT(*) AS info FROM tb_user_interest;
SELECT '' AS info;
SELECT '测试账户信息：' AS info;
SELECT '用户名: zhangsan ~ carol, bob' AS info;
SELECT '密码: 123456' AS info;
SELECT '管理员: admin / admin123' AS info;

