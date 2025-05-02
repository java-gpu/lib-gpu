#import <Cocoa/Cocoa.h>
#import <jawt.h>
#import <jawt_md.h>
#import <jni.h>
#import <iostream>
#import "jni_utils.h"
#import <objc/objc.h>
#import <objc/message.h>

extern "C" {
    typedef struct jawt_MacOSXDrawingSurfaceInfo {
        void* cglContext;   // NSOpenGLContext*
        void* cglPixelFormat;
        void* cocoaView;    // NSView*
        void* cocoaWindow;  // NSWindow*
    } JAWT_MacOSXDrawingSurfaceInfo;

    void* getNSViewFromCanvas(JNIEnv* env, jobject canvas) {
        JAWT awt;
        awt.version = JAWT_VERSION_9;

        if (!JAWT_GetAWT(env, &awt)) {
            jniLog(env, "ERROR", "macos_view.mm", "❌ JAWT_GetAWT failed");
            return nullptr;
        }

        JAWT_DrawingSurface* ds = awt.GetDrawingSurface(env, canvas);
        if (!ds) {
            jniLog(env, "ERROR", "macos_view.mm", "❌ Failed to get drawing surface");
            return nullptr;
        }

        jint lock = ds->Lock(ds);
        if ((lock & JAWT_LOCK_ERROR) != 0) {
            jniLog(env, "ERROR", "macos_view.mm", "❌ Failed to lock drawing surface");
            awt.FreeDrawingSurface(ds);
            return nullptr;
        }

        JAWT_DrawingSurfaceInfo* dsi = ds->GetDrawingSurfaceInfo(ds);
        if (!dsi) {
            jniLog(env, "ERROR", "macos_view.mm", "❌ Failed to get drawing surface info");
            ds->Unlock(ds);
            awt.FreeDrawingSurface(ds);
            return nullptr;
        }

        // On macOS with CALayer, platformInfo is NSView*
        //void* nsView = dsi->platformInfo;
        JAWT_MacOSXDrawingSurfaceInfo* dsi_mac = (JAWT_MacOSXDrawingSurfaceInfo*)dsi->platformInfo;
        void* nsView = dsi_mac->cocoaView;

        ds->FreeDrawingSurfaceInfo(dsi);
        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);

        jniLog(env, "DEBUG", "macos_view.mm", "✅ Return nsView to caller");
        return nsView;
    }

    // void* createNativeWindow(JNIEnv* env, int x, int y, int width, int height, const char* title) {
    //     __block NSWindow* windowResult = nullptr;

    //     void (^createWindowBlock)(void) = ^{
    //         @autoreleasepool {
    //             if ([NSApplication sharedApplication] == nil) {
    //                 jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "Initial sharedApplication");
    //                 [NSApplication sharedApplication];
    //             }

    //             jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "Allocate new window");
    //             windowResult = [[NSWindow alloc]
    //                 initWithContentRect:NSMakeRect(x, y, width, height)
    //                 styleMask:NSWindowStyleMaskTitled | NSWindowStyleMaskClosable | NSWindowStyleMaskResizable
    //                 backing:NSBackingStoreBuffered
    //                 defer:NO];

    //             if (windowResult == nil) {
    //                 NSLog(@"Failed to create window!");
    //                 jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "❌ Failed to create window!");
    //                 return;
    //             }

    //             jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "Setting title");
    //             NSString *nsTitle = [NSString stringWithUTF8String:title];
    //             [windowResult setTitle:nsTitle];

    //             jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "Create window with NSMakeRect");
    //             NSView *contentView = [[NSView alloc] initWithFrame:NSMakeRect(x, y, width, height)];
    //             [windowResult setContentView:contentView];
    //             [contentView release];

    //             jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "makeKeyAndOrderFront");
    //             [windowResult makeKeyAndOrderFront:nil];
    //         }
    //     };

    //     if ([NSThread isMainThread]) {
    //         createWindowBlock();
    //     } else {
    //         dispatch_sync(dispatch_get_main_queue(), createWindowBlock);
    //     }
    //     if (!windowResult) {
    //         jniLog(env, "ERROR", "macos_view.mm#createNativeWindow", "Window creation failed!");
    //         return nullptr;
    //     }
    //     jniLog(env, "DEBUG", "macos_view.mm#createNativeWindow", "return");
    //     return (void *)windowResult;
    // }
}