# IntelliBook-Mall 快速参考

## 📚 项目文档索引

### 核心设计文档
- **设计文档**: `.kiro/specs/intellibook-mall/design.md` - 完整的系统设计
- **需求文档**: `.kiro/specs/intellibook-mall/requirements.md` - 功能需求说明
- **任务列表**: `.kiro/specs/intellibook-mall/tasks.md` - 开发任务清单

### 简化方案文档
- **简化总结**: `.kiro/specs/intellibook-mall/PROJECT_SIMPLIFICATION_SUMMARY.md` - 项目简化说明
- **简化指南**: `DATABASE_SIMPLIFICATION_GUIDE.md` - 详细的简化指南
- **数据库脚本**: `database_schema_simplified_sqlite.sql` - SQLite 数据库脚本

### 文件管理文档
- **文件管理指南**: `.kiro/specs/intellibook-mall/ebook-file-management-guide.md` - 文件管理方案
- **快速入门**: `ebook-storage/QUICK_START.md` - 文件存储快速入门
- **完成总结**: `FILE_STORAGE_SETUP_COMPLETE.md` - 文件存储搭建完成

### 多语言支持文档
- **设计更新**: `DESIGN_DOCUMENT_UPDATES.md` - 多语言支持设计更新

---

## 🎯 项目关键信息

### 技术栈
- **后端**: Spring Boot 3.1.11 + MyBatis 3.5.x
- **数据库**: SQLite 3（零配置）
- **前端**: Vue.js 3.4.x + Element Plus 2.x

### 数据库表（8张）
1. `tb_user` - 用户表
2. `tb_category` - 分类表
3. `tb_ebook` - 电子书表
4. `tb_order` - 订单表
5. `tb_order_item` - 订单项表
6. `tb_cart` - 购物车表
7. `tb_favorite` - 收藏表
8. `tb_review` - 评价表

### 文件存储结构
```
ebook-storage/
├── covers/          # 封面图片
├── books/
│   ├── pdf/        # PDF 文件
│   ├── epub/       # EPUB 文件
│   └── mobi/       # MOBI 文件
└── temp/
    └── uploads/    # 临时上传
```

---

## 🚀 快速开始

### 1. 数据库配置
```properties
# application.properties
spring.datasource.url=jdbc:sqlite:intellibook.db
spring.datasource.driver-class-name=org.sqlite.JDBC
```

### 2. 初始化数据库
```bash
# 数据库会在首次运行时自动创建
# 或手动执行 SQL 脚本
sqlite3 intellibook.db < database_schema_simplified_sqlite.sql
```

### 3. 启动项目
```bash
mvn spring-boot:run
```

### 4. 测试接口
```
http://localhost:8080/api/test/hello
http://localhost:8080/api/test/storage-config
```

---

## 📊 项目简化对比

| 指标 | 原设计 | 简化方案 | 改善 |
|------|--------|----------|------|
| 数据库 | MySQL | SQLite | 零配置 |
| 表数量 | 15+ 张 | 8 张 | ⬇️ 47% |
| 代码量 | 100% | 60% | ⬇️ 40% |
| 开发时间 | 6 周 | 3 周 | ⬇️ 50% |
| 功能完整度 | 100% | 85%+ | 核心功能全保留 |

---

## 🎓 毕业设计优势

### ✅ 技术展示充分
- Spring Boot 框架
- RESTful API 设计
- 数据库设计
- 前后端分离
- 文件管理
- 多语言支持

### ✅ 答辩演示友好
- 导师可直接运行
- 无需配置数据库
- 数据可视化工具丰富
- 便于展示和讲解

### ✅ 开发效率高
- 节省 50% 开发时间
- 减少 40% 代码量
- 快速原型开发
- 易于调试测试

---

## 📝 下一步行动

### 待完成任务
- [ ] 更新 pom.xml 添加 SQLite 依赖
- [ ] 更新 application.properties 配置
- [ ] 执行数据库初始化脚本
- [ ] 准备测试文件（电子书和封面）
- [ ] 开始核心功能开发

### 开发顺序建议
1. 用户注册登录
2. 电子书 CRUD
3. 分类管理
4. 搜索功能
5. 购物车和订单
6. 收藏和评价

---

## 🔗 有用的链接

### 工具
- **DB Browser for SQLite**: https://sqlitebrowser.org/
- **SQLite 官方文档**: https://www.sqlite.org/docs.html
- **Spring Boot 文档**: https://spring.io/projects/spring-boot

### 测试资源
- **Project Gutenberg**: https://www.gutenberg.org/ (免费电子书)
- **Open Library**: https://openlibrary.org/ (免费电子书)

---

## 💡 常用命令

### Maven
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 打包项目
mvn clean package
```

### SQLite
```bash
# 打开数据库
sqlite3 intellibook.db

# 查看所有表
.tables

# 查看表结构
.schema tb_ebook

# 退出
.quit
```

### Git
```bash
# 查看状态
git status

# 提交更改
git add .
git commit -m "描述"

# 推送到远程
git push
```

---

## 📞 需要帮助？

如果遇到问题，可以：
1. 查看相关文档
2. 检查数据库连接
3. 查看控制台日志
4. 使用 DB Browser 检查数据

---

**项目状态**: ✅ 设计完成，准备开发  
**最后更新**: 2025年11月3日
