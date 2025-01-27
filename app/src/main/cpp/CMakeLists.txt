cmake_minimum_required(VERSION 3.4.1)

find_package (oboe REQUIRED CONFIG)
# include folders
include_directories(
        ${CMAKE_CURRENT_LIST_DIR}
)

# App specific sources
set(APP_SOURCES
        SplinterAudioStream.cpp
        bridge/SplinterJNI.cpp
        WaveGenerator.cpp
        bridge/NativeCallback.cpp
        bridge/jni_utils.cpp
)

# Build the minimaloboe (native) library
add_library(splintertest SHARED ${APP_SOURCES})

# Enable optimization flags: if having problems with source level debugging,
# disable -Ofast ( and debug ), re-enable after done debugging.
target_compile_options(splintertest PRIVATE -Wall -Werror "$<$<CONFIG:RELEASE>:-Ofast>")

target_link_libraries(
        splintertest
        oboe::oboe
        log
)
target_link_options(splintertest PRIVATE "-Wl,-z,max-page-size=16384")