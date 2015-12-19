package bsh;

import org.easyway.system.Core;

/**
 *
 * @author Daniele Paggi
 */
public class EasyWayInterpreter extends Interpreter {

    public EasyWayInterpreter(ConsoleInterface arg0) {
        super(arg0);
    }

    public void weakUp() {
        ready = true;
    }

    class InnerThread extends Thread {

        public InnerThread() {
            setDaemon(false);
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            int count = 0;
            while (true) {
                try {
                    while (!ready) {

                        try {
                            Thread.sleep(10L);
                            Thread.yield();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        /*if (count++ >= maxWaitCycle) {
                        ready = true;
                        }*/

                    }
                    count = 0;
                    print(getBshPrompt());
                    eof = parser.Line();
                    ready = false;

                } catch (ParseException e) {
                    error("Parser Error: " + e.getMessage(DEBUG));
                    if (DEBUG) {
                        e.printStackTrace();
                    }
                    if (!interactive) {
                        eof = true;
                    }

                    parser.reInitInput(in);
                }
            }
        }
    }

    /**
    Get the prompt string defined by the getBshPrompt() method in the
    global namespace.  This may be from the getBshPrompt() command or may
    be defined by the user as with any other method.
    Defaults to "bsh % " if the method is not defined or there is an error.
     */
    private String getBshPrompt() {
        try {
            return (String) eval("getBshPrompt()");
        } catch (Exception e) {
            return "bsh % ";
        }

    }
    int maxWaitCycle = 50;
    boolean eof = false;
    boolean firstRun = true;
    InnerThread innerThread = null;
    boolean ready = true;

    @Override
    public void run() {
        int oldTimeOut = Core.getInstance().getTimeOut();
        Core.getInstance().setTimeOut(-1);
        if (firstRun) {
            try {
                eval("printBanner();");
                eval("import org.easyway.objects.*;");
                eval("import org.easyway.objects.texture.*;");
                eval("import org.easyway.objects.sprites2D.*;");
                eval("import org.easyway.objects.sprites2D.sentry.*;");
                eval("import org.easyway.objects.animo.*;");
                eval("import org.easyway.objects.text.*;");
                eval("import org.easyway.objects.list.*;");
                eval("import org.easyway.objects.list.linked.*;");
                eval("import org.easyway.sound.*;");
                eval("import org.easyway.tiles.*;");
                eval("import org.easyway.system.*;");
                eval("import org.easyway.xmlParser.*;");
                eval("import org.easyway.util.*;");
                eval("import org.easyway.particles.*;");
                eval("import org.easyway.physics.*;");
                eval("import *;");
            } catch (EvalError e) {
                println(
                        "BeanShell " + VERSION + " - by Pat Niemeyer (pat@pat.net)");
            }

            firstRun = false;
        }

// init the callstack.
        CallStack callstack = new CallStack(globalNameSpace);

        //boolean eof = false;
        //while (!eof) {
        try {
            // try to sync up the console
            System.out.flush();
            System.err.flush();
            //Thread.yield();  // this helps a little


            if (innerThread == null) {
                (innerThread = new InnerThread()).start();
            }

            if (parser.jjtree.nodeArity() > 0) // number of child nodes
            {
                SimpleNode node = (SimpleNode) (parser.jjtree.rootNode());

                if (DEBUG) {
                    node.dump(">");
                }

                Object ret = node.eval(callstack, this);

                // sanity check during development
                if (callstack.depth() > 1) {
                    throw new InterpreterError(
                            "Callstack growing: " + callstack);
                }

                if (ret instanceof ReturnControl) {
                    ret = ((ReturnControl) ret).value;
                }

                if (ret != Primitive.VOID) {
                    setu("$_", ret);
                    //if (showResults) {
                    println("<" + ret + ">");
                    //}
                    }

                ready = true;
            }

        } catch (ParseException e) {
            error("Parser Error: " + e.getMessage(DEBUG));
            if (DEBUG) {
                e.printStackTrace();
            }

            if (!interactive) {
                eof = true;
            }

            parser.reInitInput(in);
        } catch (InterpreterError e) {
            error("Internal Error: " + e.getMessage());
            e.printStackTrace();
            if (!interactive) {
                eof = true;
            }

        } catch (TargetError e) {
            error("// Uncaught Exception: " + e);
            if (e.inNativeCode()) {
                e.printStackTrace(DEBUG, err);
            }

            if (!interactive) {
                eof = true;
            }

            setu("$_e", e.getTarget());
        } catch (EvalError e) {
            if (interactive) {
                error("EvalError: " + e.toString());
            } else {
                error("EvalError: " + e.getMessage());
            }

            if (DEBUG) {
                e.printStackTrace();
            }

            if (!interactive) {
                eof = true;
            }

        } catch (Exception e) {
            error("Unknown error: " + e);
            if (DEBUG) {
                e.printStackTrace();
            }

            if (!interactive) {
                eof = true;
            }

        } catch (TokenMgrError e) {
            error("Error parsing input: " + e);

            /*
            We get stuck in infinite loops here when unicode escapes
            fail.  Must re-init the char stream reader
            (ASCII_UCodeESC_CharStream.java)
             */
            parser.reInitTokenInput(in);

            if (!interactive) {
                eof = true;
            }

        } finally {
            parser.jjtree.reset();
            // reinit the callstack
            if (callstack.depth() > 1) {
                callstack.clear();
                callstack.push(globalNameSpace);
            }
            Core.getInstance().setTimeOut(oldTimeOut);
        }
        //}

        /*if (interactive && exitOnEOF) {
        System.exit(0);
        }*/
    }
}
