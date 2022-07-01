@echo off
set nowPath=%cd%
cd /
cd %nowPath%

::删除指定文件 *.iml,*.iml.* 格式文件
for /r %nowPath% %%i in (*.iml) do (del %%i)

::删除指定文件夹.idea,target文件夹
for /r %nowPath% %%i in (.idea,target) do (IF EXIST %%i RD /s /q %%i)

echo OK
pause

::参数解释：
::  	del：为删除cmd（命令行）删除命令
::  	pause：默认不加pause窗口会一闪而过（文件会删除），此参数行窗口停顿作用。
::del 携带参数介绍：
::  	/q：表示删除时是否对用户进行询问（yes or no）
::  	/f：表示强制删除只读文件，无需确认
::  	/s：表示从当前目录及其所有子目录中删除指定文件。显示正在被删除的文件名。
::  	/a：表示根据指定的属性删除文件。
::  	/p：表示del 将显示文件名，并发送如下消息：filename, Delete (Y/N)?按 Y 确认删除，N 取消删除并显示下一个文件名（如果指定了一组文件），或者按 CTRL+C 停止 del 命令。
::  注：在命令行使用del命令删除文件，无法恢复该文件。