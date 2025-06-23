package tech.lib.ui.input.queue;

import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.app.AwtAppWindow;
import tech.lib.ui.event.DropFileEvent;
import tech.lib.ui.jni.EventManager;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

@Slf4j
public class DropTargetAsFileQueue extends java.awt.dnd.DropTarget {
    public DropTargetAsFileQueue(AwtAppWindow appWindow, int ops) throws HeadlessException {
        super(appWindow, ops, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(ops);
                    Transferable transferable = event.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked") java.util.List<File> droppedFiles =
                                (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : droppedFiles) {
                            log.debug("Dropped file: {}", file.getAbsolutePath());
                            EventManager.pushUiEvent(new DropFileEvent(file.getAbsolutePath(), appWindow.getWindowPointer()));
                        }
                    }

                    event.dropComplete(true);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    event.dropComplete(false);
                }
            }
        });
    }
}
