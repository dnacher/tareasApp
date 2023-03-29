@echo creando respaldo
@echo off
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I
set datetime=%datetime:~0,8%
set nombre=tareas-app_%datetime%.sql
mysqldump -u root -proot tareas-app > C:\Desarrollo\testdatabase\%nombre%

