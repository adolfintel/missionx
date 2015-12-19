#include<stdio.h>
#include<unistd.h>
main(){
printf("Loading Mission X for Linux\n");
execl("jreLinux/bin/java","-Xms512m","-Djava.library.path=.","-jar","MyGame.jar",NULL);
}
