@echo off
echo Setting up Bank Management System tables in Aiven MySQL...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --user=avnadmin --host=mysql-2494552c-shivakumarnayak2005-012f.i.aivencloud.com --port=13685 -p defaultdb < "database_setup.sql"
if %ERRORLEVEL% EQU 0 (
    echo.
    echo All tables created successfully!
) else (
    echo.
    echo Failed to create tables. Please check your password and connection.
)
pause
