#include <jni.h>
#include "gb.h"

static GB_gameboy_t gb;

extern "C" JNIEXPORT void JNICALL
Java_com_example_glassgameboyemulator_MainActivity_initEmulator(JNIEnv* env, jobject obj) {
    GB_init(&gb, GB_MODEL_DMG_B);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_glassgameboyemulator_MainActivity_loadRom(JNIEnv* env, jobject obj, jstring romPath) {
    const char* path = env->GetStringUTFChars(romPath, nullptr);
    GB_load_rom(&gb, (uint8_t*)path);
    env->ReleaseStringUTFChars(romPath, path);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_glassgameboyemulator_MainActivity_runFrame(JNIEnv* env, jobject obj) {
    GB_run_frame(&gb);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_glassgameboyemulator_MainActivity_setButtonState(JNIEnv* env, jobject obj, jint button, jboolean pressed) {
    switch (button) {
        case 0: GB_set_key_state(&gb, GB_KEY_UP, pressed); break;
        case 1: GB_set_key_state(&gb, GB_KEY_DOWN, pressed); break;
        case 2: GB_set_key_state(&gb, GB_KEY_LEFT, pressed); break;
        case 3: GB_set_key_state(&gb, GB_KEY_RIGHT, pressed); break;
        case 4: GB_set_key_state(&gb, GB_KEY_A, pressed); break;
        case 5: GB_set_key_state(&gb, GB_KEY_B, pressed); break;
        case 6: GB_set_key_state(&gb, GB_KEY_START, pressed); break;
        case 7: GB_set_key_state(&gb, GB_KEY_SELECT, pressed); break;
    }
}