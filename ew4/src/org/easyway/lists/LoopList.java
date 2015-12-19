/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class LoopList
        extends BaseList<ILoopable<LoopEntry>, LoopEntry>
        implements ILoopable<LoopEntry>, Serializable {

    private static final long serialVersionUID = 207123540807333452L;

    /** creates a new instance of LoopList */
    public LoopList() {
        super();
        setType("$_LOOPLIST");
    }

    /** creates a new instance of LoopList */
    public LoopList(boolean toadd) {
        super(toadd);
        setType("$_LOOPLIST");
    }

    @Override
    protected LoopEntry createEntry(LoopEntry next, LoopEntry prev, ILoopable<LoopEntry> value, BaseList list) {
        if (StaticRef.DEBUG && contains(value)) {
            throw new RuntimeException("Object already contained! Check your code!");
        }
        return new LoopEntry(next, prev, value, list);
    }
    // -----------------------------------------
    static ArrayList<ILoopable> loops[];

    public static void init() {
        loops = new ArrayList[Core.getInstance().getNumberCpu()];
        for (int i = 0; i < Core.getInstance().getNumberCpu(); ++i) {
            loops[i] = new ArrayList<ILoopable>(1000);
        }
    }

    /** loops all sprites */
    public void loop() {
        ILoopable lobj;
        int nCpu = Core.getInstance().getNumberCpu();
        if (nCpu == 1) {
            startScan();
            while (next()) {
                lobj = getCurrent();
                assert lobj != null : "null object in the LoopList!";
                if (lobj.isDestroyed()) {
                    System.out.println("BUG founded! Already destroied item found in the LoopList!   " + lobj + " class:" + lobj.getClass());
                    new RuntimeException().printStackTrace();
                }
                lobj.loop();
            }
        } else {

            int i = 0;
            for (; i < nCpu; ++i) {
                loops[i].clear();
            }

            i = 0;
            startScan();
            while (next()) {
                lobj = getCurrent();
                loops[i].add(lobj);
                if (++i >= nCpu) {
                    i = 0;
                }
            }
            LoopThread[] thresds = new LoopThread[nCpu];
            for (i = 0; i < nCpu; ++i) {
                (thresds[i] = new LoopThread(loops[i])).start();
            }
            for (i = 0; i < nCpu; ++i) {
                try {
                    thresds[i].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoopList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class LoopThread extends Thread {

        ArrayList<ILoopable> loopList;

        public LoopThread(ArrayList<ILoopable> list) {
            this.loopList = list;
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            for (ILoopable loop : loopList) {
                loop.loop();
            }
        }
    }
}
