cmake_minimum_required(VERSION 3.10.2)
project(SameBoy)

add_library(sameboy SHARED
            gb.c
            memory.c
            display.c
            apu.c
)

target_include_directories(sameboy PRIVATE .)
find_library(log-lib log)
target_link_libraries(sameboy ${log-lib})