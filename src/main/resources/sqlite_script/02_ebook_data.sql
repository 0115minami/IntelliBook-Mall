-- ============================================
-- 电子书数据初始化脚本（更新版）
-- 符合 IntelliBook-Mall 表结构设计
-- 注意：需要先执行 03_minimal_required_data.sql 初始化分类数据
-- 文件存储结构：ebook-storage/books/{format}/{book_id}.{format}
-- 封面存储结构：ebook-storage/covers/{book_id}.jpg
-- ============================================

-- 插入电子书数据（符合 tb_ebook 表结构）
-- 注意：category_id 需要对应已存在的分类
-- 注意：book_id 会自动生成，从1开始递增
INSERT INTO tb_ebook (book_title, author, isbn, publisher, publish_date, book_intro, 
                      category_id, cover_img, page_count, price, language, tags, rating, rating_count, status) VALUES
-- 1. 深度学习入门 (计算机 > 人工智能，category_id=17)
('深度学习入门:基于 Python 的理论与实现', '斋藤康毅', '9787115485588', '人民邮电出版社', '2018-07-01',
 '《深度学习入门：基于Python的理论与实现》是深度学习真正意义上的入门书，深入浅出地剖析了深度学习的核心理论。',
 17, 'covers/1.jpg', 336, 5900, 'zh-CN', 'Python,深度学习,机器学习,神经网络', 4.6, 890, 1),

-- 2. 红玫瑰与白玫瑰 (文学 > 现代文学，category_id=23)
('红玫瑰与白玫瑰', '张爱玲', '9787530218617', '北京十月文艺出版社', '2019-01-01',
 '《红玫瑰与白玫瑰》是张爱玲的经典作品之一，讲述了男人心中永恒的两难选择。',
 23, 'covers/2.jpg', 256, 2800, 'zh-CN', '张爱玲,现代文学,爱情小说', 4.5, 3420, 1),

-- 3. TOEFL iBT 备考指南 (教育 > 外语学习，category_id=30)
('TOEFL iBT Preparation Book: Test Prep for Reading, Listening, Speaking, & Writing', 'Test Prep Books', '9781628454246', 
 'Test Prep Books', '2017-04-17',
 'A comprehensive study guide for the TOEFL iBT exam, covering all four sections with practice tests and strategies.',
 30, 'covers/3.jpg', 412, 3900, 'en-US', 'TOEFL,English,Test Preparation,Study Guide', 4.2, 567, 1),

-- 4. 国富论 (经济管理 > 经济学，category_id=29)
('国富论', '亚当·斯密', '9787508036083', '华夏出版社', '2005-01-01',
 '现代经济学的开山之作，系统阐述了自由市场经济理论，被誉为经济学的"圣经"。',
 29, 'covers/4.jpg', 1200, 6800, 'zh-CN', '经济学,古典经济学,亚当斯密,市场经济', 4.7, 2150, 1),

-- 5. 心：稻盛和夫的一生嘱托 (经济管理 > 企业管理，category_id=25)
('心：稻盛和夫的一生嘱托', '稻盛和夫', '9787115536198', '人民邮电出版社', '2020-05-01',
 '"稻盛哲学"集大成之作，讲述企业经营和人生哲学的智慧。',
 25, 'covers/5.jpg', 288, 4900, 'zh-CN', '稻盛和夫,企业管理,经营哲学,人生智慧', 4.8, 1680, 1),

-- 6. 飞鸟集 (文学 > 文学经典，category_id=21)
('飞鸟集', '泰戈尔', '9787544710985', '译林出版社', '2010-06-01',
 '本书精选泰戈尔最负盛名的代表作，共收录泰戈尔的三部散文诗集：《飞鸟集》、《吉檀迦利》和《园丁集》。',
 21, 'covers/6.jpg', 320, 2400, 'zh-CN', '泰戈尔,诗歌,散文诗,印度文学', 4.9, 5670, 1),

-- 7. 中国野菜图鉴 (生活健康 > 美食烹饪，category_id=34)
('中国野菜图鉴 (中国之美自然生态图鉴)', '刘全儒', '9787537750264', '山西科学技术出版社', '2015-04-01',
 '《中国野菜图鉴》是"中国之美 自然生态图鉴"系列作品中的一部，详细介绍了中国常见野菜的识别和食用方法。',
 34, 'covers/7.jpg', 256, 5800, 'zh-CN', '野菜,植物图鉴,美食,自然', 4.4, 432, 1),

-- 8. 茹素之乐 (生活健康 > 美食烹饪，category_id=34)
('茹素之乐: 美味素食菜谱100例', '亚历克斯·巴拉克斯', NULL, '浙江出版联合集团', '2020-01-01',
 '100道令人垂涎欲滴的素食菜谱，为您的生活带去奇妙的转变。健康、美味、环保的素食生活方式指南。',
 34, 'covers/8.jpg', 180, 3800, 'zh-CN', '素食,菜谱,健康饮食,烹饪', 4.3, 289, 1),

-- 9. Git for Teams (计算机 > 前端开发，category_id=12)
('Git for Teams: A User-Centered Approach to Creating Efficient Workflows in Git', 'Emma Jane Hogbin Westby', '9781491911181', 
 'O''Reilly Media', '2015-10-01',
 'Learn how to use Git in a team environment. This book provides practical strategies for managing Git workflows and collaboration.',
 12, 'covers/9.jpg', 368, 4500, 'en-US', 'Git,Version Control,Team Collaboration,DevOps', 4.3, 234, 1),

-- 10. 幽灵塔 (文学 > 悬疑推理，category_id=20)
('幽灵塔', '江户川乱步', '9787561352489', '陕西师范大学出版总社有限公司', '2012-06-01',
 '日本推理小说之父江户川乱步的经典作品，讲述了一个充满悬疑和恐怖的推理故事。',
 20, 'covers/10.jpg', 288, 3200, 'zh-CN', '江户川乱步,推理小说,悬疑,日本文学', 4.4, 1567, 1),

-- 11. 小说写作：叙事技巧指南 (教育 > 职业技能，category_id=31)
('小说写作：叙事技巧指南', '珍妮特·伯罗薇', '9787300189437', '中国人民大学出版社', '2014-09-01',
 '一本全面系统的小说写作指南，从叙事技巧、人物塑造到情节构建，帮助写作者提升创作能力。',
 31, 'covers/11.jpg', 432, 5200, 'zh-CN', '写作技巧,小说创作,叙事,文学创作', 4.6, 678, 1),

-- 12. 东京梦华录 (历史人文 > 历史，category_id=6)
('东京梦华录', '杨春俏', '9787101089523', '中华书局', '2013-01-01',
 '北宋孟元老所著，记录了北宋都城东京（今开封）的城市风貌、市井生活和民俗风情，是研究宋代社会的重要文献。',
 6, 'covers/12.jpg', 256, 3600, 'zh-CN', '宋代,历史,东京,民俗,古籍', 4.5, 892, 1);

-- 插入电子书文件数据（符合 tb_ebook_file 表结构）
-- 注意：file_path 格式为 books/{format}/{book_id}.{format}
-- 根据 ebook-storage 目录实际文件结构配置
INSERT INTO tb_ebook_file (book_id, file_format, file_path, file_size) VALUES
-- 书籍1 - PDF, EPUB
(1, 'PDF', 'books/pdf/1.pdf', 12345600),
(1, 'EPUB', 'books/epub/1.epub', 10234500),

-- 书籍2 - EPUB
(2, 'EPUB', 'books/epub/2.epub', 3456700),

-- 书籍3 - PDF
(3, 'PDF', 'books/pdf/3.pdf', 8765400),

-- 书籍4 - PDF, EPUB
(4, 'PDF', 'books/pdf/4.pdf', 20123400),
(4, 'EPUB', 'books/epub/4.epub', 18765400),

-- 书籍5 - PDF, EPUB
(5, 'PDF', 'books/pdf/5.pdf', 6234500),
(5, 'EPUB', 'books/epub/5.epub', 5678900),

-- 书籍6 - PDF, EPUB
(6, 'PDF', 'books/pdf/6.pdf', 5123400),
(6, 'EPUB', 'books/epub/6.epub', 4567800),

-- 书籍7 - PDF, EPUB
(7, 'PDF', 'books/pdf/7.pdf', 17234500),
(7, 'EPUB', 'books/epub/7.epub', 15678900),

-- 书籍8 - EPUB
(8, 'EPUB', 'books/epub/8.epub', 8765400),

-- 书籍9 - PDF
(9, 'PDF', 'books/pdf/9.pdf', 9876500),

-- 书籍10 - EPUB
(10, 'EPUB', 'books/epub/10.epub', 6543200),

-- 书籍11 - PDF, EPUB
(11, 'PDF', 'books/pdf/11.pdf', 11234500),
(11, 'EPUB', 'books/epub/11.epub', 9876500);

-- 数据插入完成
SELECT '=== 电子书数据初始化完成 ===' AS info;
SELECT '电子书数量: ' || COUNT(*) AS info FROM tb_ebook;
SELECT '电子书文件数量: ' || COUNT(*) AS info FROM tb_ebook_file;
SELECT '提示：请确保 ebook-storage 目录中存在对应的文件' AS info;
