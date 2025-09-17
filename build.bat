@echo off
echo Building BubblePerks...
mvn clean package
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful! JAR file is located in the target directory.
    echo.
    if exist "target\BubblePerks-*.jar" (
        for %%f in (target\BubblePerks-*.jar) do (
            echo JAR location: %%f
        )
    )
) else (
    echo.
    echo Build failed! Check the output above for errors.
)
pause
