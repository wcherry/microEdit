@echo off
if "%GROOVY_HOME%" = "" goto NO_GH
if not exist %GROOVY_HOME% goto NO_GH_DIR
set JAVA_ARGS=-Djava.system.class.loader=org.sss.micro.bootstrap.CustomClassLoader
set CP=%~dp0..\bootstrap\bootstrap.jar;%~dp0..\plugins\core.jar;%GROOVY_HOME%\embeddable\groovy-all-2.0.0-beta-3.jar;%GROOVY_HOME%\lib\asm-4.0.jar
echo %CP%
java %JAVA_ARGS% -classpath %CP% org.sss.micro.bootstrap.Application -d %~dp0\..
goto :EOF
:NO_GH
echo GROOVY_HOME not set, please set GROOVY_HOME to point to your Groovy installation directory.
goto :EOF
:NO_GH_DIR
echo GROOVY_HOME (%GROOVY_HOME%) not does not exits, please set GROOVY_HOME to point to your Groovy installation directory.
goto :EOF



