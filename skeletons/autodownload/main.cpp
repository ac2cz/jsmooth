/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003-2007 Rodrigo Reyes <reyes@charabia.net>

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.
  
  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free
  Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
  
*/

#include <windows.h>
#include <iostream>
#include <fstream>

#include "resource.h"
#include "common.h"
#include "ResourceManager.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"

#include "httpdownload.h"
#include "execcab.h"


ResourceManager* globalResMan;
DebugConsole *DEBUGCONSOLE = NULL;

void lastExit()
{
  delete DEBUGCONSOLE;
  DEBUGCONSOLE = 0;
  delete globalResMan;
  globalResMan = 0;
}

void _debugOutput(const std::string& text)
{
  if (DEBUGCONSOLE != NULL)
    DEBUGCONSOLE->writeline(text);
  printf("%s\n", text.c_str());
  fflush(stdout);
}

void _debugWaitKey()
{
  if (DEBUGCONSOLE != NULL)
    DEBUGCONSOLE->waitKey();
}

int WINAPI WinMain (HINSTANCE hThisInstance,
                    HINSTANCE hPrevInstance,
                    LPSTR lpszArgument,
                    int nFunsterStil)
{
  atexit(lastExit);

  globalResMan = new ResourceManager("JAVA", PROPID, JARID);

  // sets up the command line arguments
  // not sure if lpszArgument can be null on Windows...
  if ((lpszArgument!=NULL) && (strlen(lpszArgument)>0))
    {
      // Note that this overwrites an existing KEY_ARGUMENTS
      std::vector<std::string> args = StringUtils::split(lpszArgument, " \t\n\r", "\"'", true);
      globalResMan->setUserArguments( args );
    }

  std::string dodebug = globalResMan->getProperty("skel_Debug");
  if (StringUtils::parseInt(dodebug) != 0)
  {
    DEBUGCONSOLE = new DebugConsole("JSmooth Debug");
    globalResMan->printDebug();
  }

  DEBUG(string("Main class: ") + globalResMan->getMainName());


  char curdir[256];
  GetCurrentDirectory(256, curdir);
  DEBUG(string("Currentdir: ") + curdir);

  string newcurdir = globalResMan->getProperty(ResourceManager::KEY_CURRENTDIR);
  SetCurrentDirectory(newcurdir.c_str());

  JavaMachineManager man(*globalResMan);
  std::string preferDLLstr = globalResMan->getProperty("skel_SingleProcess");

  bool preferDLL = false;
  if (StringUtils::parseInt(preferDLLstr) > 0)
    preferDLL = true;

  if (man.run(true, preferDLL) == false)
    {
      DEBUG("Displaying error message to user...");
      std::string errmsg = globalResMan->getProperty("skel_Message");
      std::string url = globalResMan->getProperty("skel_DownloadURL");

      DEBUG("URL=" + url);
      if ((url != "") && (MessageBox(NULL, errmsg.c_str(), "No Java?", MB_OKCANCEL|MB_ICONQUESTION|MB_APPLMODAL) == IDOK))
	{
	  // string file = httpDownload("http://java.sun.com/products/plugin/autodl/jinstall-1_3_0_05-win.cab");
	  DEBUG("Now downloading " + url);
	  string file = httpDownload(url);
	  printf("GOT FILE[%s]\n", file.c_str());
	  
	  if (file != "")
	    {
	      string ext = StringUtils::toLowerCase(FileUtils::getFileExtension(file));
	      if (ext == "cab")
		exec_cab(file);
	      else if (ext == "exe")
		{
		  DEBUG("Executing EXE");
		  execute(file, true);
		}
	      else // anything else, we try to open it like a document
		{
		  ShellExecute(NULL, "open", file.c_str(), NULL, "", 0);
		}
	    }
	  else
	    {
	      MessageBox(NULL, "Can't download the Java Environement.\nPlease retry later...", "Download failed", MB_OK|MB_APPLMODAL);
	    }
	}
    }


  DEBUG("NORMAL EXIT");
  DEBUGWAITKEY();

  /* The program return-value is 0 - The value that PostQuitMessage() gave */
  return 0;
}
