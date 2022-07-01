````shell
feat:添加logback配置

============================git注释规范============================
br: 此项特别针对bug号，用于向测试反馈bug列表的bug修改情况
feat：新功能（feature）
fix：修补
docs：文档（documentation）
style： 格式（不影响代码运行的变动）
refactor：重构（即不是新增功能，也不是修改bug的代码变动）
test：增加测试
chore：其他的小改动. 一般为仅仅一两行的改动, 或者连续几次提交的小改动属于这种
revert：feat(pencil): add 'graphiteWidth' option (撤销之前的commit)
upgrade：升级改造
bugfix：修补bug
optimize：优化
perf: Performance的缩写, 提升代码性能
test：新增测试用例或是更新现有测试
ci:主要目的是修改项目继续完成集成流程(例如Travis，Jenkins，GitLab CI,Circle)的提交
build: 主要目的是修改项目构建系统(例如glup，webpack，rollup的配置等)的提交
==============================================================
每个git commit记录都需要按照固定格式，具体格式为：
// 头header
<type>(<scope>): <subject>
// 空一行
<body>
// 空一行
<footer>

例子：
feature(数据层): 简短描述

详细描述

BREAKING CHANGE: 不兼容变动

Closes 关闭issue
============================git命令============================
git init 初始化项目
git remote add origin [远程库地址]
git pull origin master
git add 添加功能
git commit -m "注释"
git push origin master

其他：
git status
git log 查看历史记录
git branch
git checkout
git merge

1.使用git status指令查看当前文件状态。
2.然后，使用指令git stash 将文件修改缓存。
3.使用git status指令确认当前分支没有修改内容。
4.使用指令git stash list，查看当前缓存列表。
5.使用指令git stash apply stash@{id}，恢复指令ID的缓存内容，并且保留缓存条目。
6.使用git stash pop 恢复最新的stash，同时删除恢复的缓存条目。
7.使用rebase进入历史提交记录
git rebase -i HEAD~n //n表示最近提交记录个数，把pick改成edit、reword等，
pick：会在你的历史记录中保留该提交。
reword：允许你修改提交信息，可能是修复一个错别字或添加其它注释。44
edit：允许你在重放分支的过程中对提交进行修改。
squash：可以将多个提交合并为一个。
使用reword，修改注释,重新push一次即可,git push -f,切记一定要加-f，否则我们edit的commit会添加到commit后面，不是更新原commit。
使用edit，保存完了之后，git的分支就会发生改变，从原来的master改成了我们第一个edit的commit id
轮流使用  git commit --amend      git rebase --continue
git commit --allow-empty 如果是空文件
git push -f
git push --force origin master
git rebase -i b2f82ab0 54176f7c
8.git cherry-pick ，它会获取某一个分支的单笔提交

fix(backend): 过滤掉没有评委的项目


git commit 未push 修改备注
未push比较简单
① $ git commit --amend  进行修改
② 按esc退出编辑模式，输入:wq保存并退出  注意中文esc请切换至英文
③ $ git push 即可

git commit push 修改备注

	①--①修改倒数第1次的commit
	指令：$ git rebase -i HEAD~1  
	
	①--②修改commit --id
	指令：$ git rebase -i commit
	
	-- 如果突然不想修改：$ git rebase --abort 
 
	②修改pick为edit
 
	   ①回车后进入一个页面，
 
	   按i进入编辑模式，
 
	   将要修改的那一条的pick修改成edit，
 
	   按esc退出编辑模式，输入:wq保存并退出
 
	③修改commit注释内容
 
	指令： $ git commit --amend
 
	按i进入编辑模式，修改内容，退出编辑模式，输入:wq保存并退出
 
	④$ git rebase --continue
 
	⑤强制push---如无人修改可。否则请检查文件
 
	$ git push --force github
````
