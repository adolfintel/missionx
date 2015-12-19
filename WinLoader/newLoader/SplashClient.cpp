#include "splash.h"
#include<unistd.h>
int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{
	CSplash splash1(TEXT("Splash.bmp"), RGB(256,256,256));
	splash1.ShowSplash();
	ShellExecute(NULL,"open","jre\\bin\\javaw.exe","-Xms512m -jar Mygame.jar",".",SW_SHOW);
	Sleep(120000); //2 minutes before the splash screen terminates automatically
	splash1.CloseSplash();
	return 0;
}
