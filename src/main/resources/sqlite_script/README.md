# IntelliBook-Mall 数据库脚本说明

## 📁 脚本文件说明

### 1. `01_schema_only.sql` - 纯表结构脚本
**用途**: 生产环境初始化  
**内容**: 
- 16张数据表的完整结构
- 所有索引定义
- 触发器定义
- **不包含任何数据**

**适用场景**:
- 生产环境首次部署
- 需要手动导入数据的场景
- 数据库结构升级

**使用方法**:
```bash
# 使用 SQLite 命令行
sqlite3 intellibook.db < 01_schema_only.sql

# 或使用 DB Browser for SQLite 导入
```

---

### 2. `02_dev_test_data.sql` - 开发测试数据脚本
**用途**: 开发和测试环境  
**内容**:
- ✅ 管理员账户（admin / admin123）
- ✅ 测试用户账户（testuser, zhangsan, lisi / 123456）
- ✅ 完整的分类体系（一级+二级分类）
- ✅ 7本示例电子书（中英文技术书+文学作品）
- ✅ 电子书文件记录（多格式支持）
- ✅ 示例书单数据
- ✅ 示例订单数据
- ✅ 示例评价和收藏数据
- ✅ 示例阅读进度数据

**适用场景**:
- 本地开发环境
- 功能测试
- 演示和答辩

**使用方法**:
```bash
# 先执行表结构脚本，再执行数据脚本
sqlite3 intellibook.db < 01_schema_only.sql
sqlite3 intellibook.db < 02_dev_test_data.sql
```

**测试账户信息**:
| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | 管理员 | 后台管理 |
| testuser | 123456 | 普通用户 | 已购买1本书 |
| zhangsan | 123456 | 普通用户 | 已购买1本书 |
| lisi | 123456 | 普通用户 | 新用户 |

---

### 3. `03_minimal_required_data.sql` - 最小必需数据脚本
**用途**: 生产环境最小化部署  
**内容**:
- ✅ 管理员账户（admin / admin123）
- ✅ 完整的分类体系（8个一级分类 + 35个二级分类）
- ❌ 不包含示例电子书
- ❌ 不包含测试用户
- ❌ 不包含示例订单

**适用场景**:
- 生产环境首次部署
- 需要从零开始添加电子书
- 最小化系统初始化

**使用方法**:
```bash
# 先执行表结构脚本，再执行最小数据脚本
sqlite3 intellibook.db < 01_schema_only.sql
sqlite3 intellibook.db < 03_minimal_required_data.sql
```

**⚠️ 重要提示**: 生产环境部署后请立即修改管理员密码！

---

## 🚀 快速开始

### 开发环境初始化（推荐）

```bash
# 1. 创建数据库并初始化表结构和测试数据
cd src/main/resources/sqlite_script
sqlite3 ../intellibook.db < 01_schema_only.sql
sqlite3 ../intellibook.db < 02_dev_test_data.sql

# 2. 验证数据
sqlite3 ../intellibook.db "SELECT COUNT(*) FROM tb_user;"
sqlite3 ../intellibook.db "SELECT COUNT(*) FROM tb_ebook;"
```

### 生产环境初始化

```bash
# 1. 创建数据库并初始化表结构和最小数据
cd src/main/resources/sqlite_script
sqlite3 ../intellibook.db < 01_schema_only.sql
sqlite3 ../intellibook.db < 03_minimal_required_data.sql

# 2. 修改管理员密码（重要！）
# 登录后台后立即修改
```

---

## 📊 数据库表结构概览

### 核心业务表（9张）
1. **tb_user** - 用户表（含管理员）
2. **tb_category** - 电子书分类表
3. **tb_ebook** - 电子书表（核心）
4. **tb_ebook_file** - 电子书文件表（多格式支持）
5. **tb_order** - 订单表
6. **tb_order_item** - 订单项表
7. **tb_cart** - 购物车表
8. **tb_favorite** - 收藏表
9. **tb_review** - 评价表

### 扩展功能表（7张）
10. **tb_review_like** - 评价点赞表
11. **tb_booklist** - 书单表
12. **tb_booklist_item** - 书单-书籍关联表
13. **tb_booklist_follow** - 书单关注表
14. **tb_user_interest** - 用户兴趣表
15. **tb_reading_progress** - 阅读进度表
16. **tb_download_record** - 下载记录表

**总计**: 16张表

---

## 🔐 默认账户信息

### 管理员账户
- **用户名**: admin
- **密码**: admin123
- **MD5**: 0192023a7bbd73250516f069df18b500
- **邮箱**: admin@intellibook.com

### 测试用户账户（仅开发环境）
- **用户名**: testuser / zhangsan / lisi
- **密码**: 123456
- **MD5**: e10adc3949ba59abbe56e057f20f883e

---

## 📚 示例数据说明（开发环境）

### 电子书数据
| 书名 | 作者 | 分类 | 语言 | 格式 | 价格 |
|------|------|------|------|------|------|
| Java 编程思想（第4版） | Bruce Eckel | Java | 中文 | PDF, EPUB | ¥88.00 |
| 算法导论（第3版） | Thomas H. Cormen | 算法 | 中文 | PDF | ¥108.00 |
| 深入理解计算机系统 | Randal E. Bryant | 计算机 | 中文 | PDF | ¥139.00 |
| Clean Code | Robert C. Martin | Java | 英文 | PDF, EPUB | ¥49.00 |
| Design Patterns | Erich Gamma | Java | 英文 | PDF | ¥59.00 |
| 三体 | 刘慈欣 | 科幻 | 中文 | EPUB, MOBI | ¥18.00 |
| 活着 | 余华 | 现代文学 | 中文 | EPUB | ¥12.00 |

### 分类体系
- **一级分类**: 8个（计算机、文学、经济、教育、生活、历史、科学、艺术）
- **二级分类**: 35个（涵盖主流领域）

---

## 🛠️ 常用操作

### 查看数据库信息
```sql
-- 查看所有表
.tables

-- 查看表结构
.schema tb_ebook

-- 统计数据
SELECT '用户数量: ' || COUNT(*) FROM tb_user;
SELECT '电子书数量: ' || COUNT(*) FROM tb_ebook;
SELECT '分类数量: ' || COUNT(*) FROM tb_category;
```

### 重置数据库
```bash
# 删除数据库文件
rm intellibook.db

# 重新初始化
sqlite3 intellibook.db < 01_schema_only.sql
sqlite3 intellibook.db < 02_dev_test_data.sql
```

### 备份数据库
```bash
# 备份
cp intellibook.db intellibook_backup_$(date +%Y%m%d).db

# 或使用 SQLite 命令
sqlite3 intellibook.db ".backup intellibook_backup.db"
```

---

## ⚠️ 注意事项

### 生产环境部署
1. ✅ 使用 `01_schema_only.sql` + `03_minimal_required_data.sql`
2. ✅ 立即修改管理员密码
3. ✅ 定期备份数据库
4. ✅ 设置数据库文件权限（只读给应用）
5. ❌ 不要使用测试数据脚本

### 开发环境
1. ✅ 使用 `01_schema_only.sql` + `02_dev_test_data.sql`
2. ✅ 可以随时重置数据库
3. ✅ 测试账户密码简单，方便测试
4. ❌ 不要在生产环境使用

### 密码安全
- 所有密码都使用 MD5 加密存储
- 生产环境必须使用强密码
- 建议定期更换管理员密码

---

## 📖 相关文档

- [数据库设计文档](../../.kiro/specs/intellibook-mall/design.md)
- [数据库改进总结](../../.kiro/specs/intellibook-mall/DATABASE_IMPROVEMENTS.md)
- [需求文档](../../.kiro/specs/intellibook-mall/requirements.md)

---

## 🤝 贡献

如需添加更多示例数据或改进脚本，请参考现有格式进行修改。

---

**最后更新**: 2024-01-17  
**版本**: v1.0.0
