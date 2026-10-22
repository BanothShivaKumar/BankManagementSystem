@echo off
echo Testing connection to Aiven MySQL...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --user=avnadmin --host=mysql-2494552c-shivakumarnayak2005-012f.i.aivencloud.com --port=13685 -p defaultdb -e "select 1 + 2 as three;"
pause
