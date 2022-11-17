#!/usr/bin/env bash
# 遍历删除当前目录下指定名称的文件夹（-type d 来指定是删除文件夹）
find . -name '*target' -type d -print -exec rm -rf {} \;
find . -name '*.idea' -type d -print -exec rm -rf {} \;

# 遍历删除当前目录下指定名称的文件（-type f 来指定是删除文件）
find . -name '*.iml' -type f -print -exec rm -rf {} \;