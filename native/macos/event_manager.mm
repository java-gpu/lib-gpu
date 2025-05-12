#import <Foundation/Foundation.h>
#import <Metal/Metal.h>
#include "event_manager_jni.h"
#include <jni.h>
#include "jni_utils.h"
#import <Cocoa/Cocoa.h>

extern "C" {
    void registerForSuspendEventsOnMac(JNIEnv* env, void* windowHandler) {
        [[NSNotificationCenter defaultCenter] addObserverForName:NSWorkspaceWillSleepNotification
                                        object:nil
                                        queue:nil
                                    usingBlock:^(NSNotification *notification) {
            // Handle suspend event (system is about to sleep)
            JNIEnv* env;
            jvm->AttachCurrentThread((void**)&env, NULL);
            // Call your JNI method
            submitSuspendStateToJava(env, SuspendState::WillSuspend);
        }];

        [[NSNotificationCenter defaultCenter] addObserverForName:NSWorkspaceDidWakeNotification
                        object:nil
                        queue:nil
                    usingBlock:^(NSNotification *notification) {
            // Handle wake event (system has woken up)
            JNIEnv* env;
            jvm->AttachCurrentThread((void**)&env, NULL);
            // Call your JNI method
            submitSuspendStateToJava(env, SuspendState::WillResume);
        }];
    }
}
