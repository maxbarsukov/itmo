cmake_minimum_required(VERSION 3.12)

project(image-transformer LANGUAGES C)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

if(CMAKE_C_COMPILER_ID STREQUAL GNU)
    set(CMAKE_CONFIGURATION_TYPES Debug Release ASan LSan UBSan)
elseif(CMAKE_C_COMPILER_ID MATCHES Clang)
    set(CMAKE_CONFIGURATION_TYPES Debug Release ASan LSan MSan UBSan)
elseif(MSVC)
    set(CMAKE_CONFIGURATION_TYPES Debug Release ASan)
endif()

if(CMAKE_BUILD_TYPE AND NOT CMAKE_BUILD_TYPE IN_LIST CMAKE_CONFIGURATION_TYPES)
    message(FATAL_ERROR "Unexpected build type ${CMAKE_BUILD_TYPE}, possible values: ${CMAKE_CONFIGURATION_TYPES}")
endif()

if(CMAKE_C_COMPILER_ID STREQUAL GNU OR CMAKE_C_COMPILER_ID MATCHES Clang)
    set(CMAKE_C_FLAGS       "-std=c17 -pedantic -Wall -Werror -ggdb")
    set(CMAKE_C_FLAGS_ASAN  "${CMAKE_C_FLAGS_DEBUG} -fsanitize=address -fno-optimize-sibling-calls -fno-omit-frame-pointer")
    set(CMAKE_C_FLAGS_LSAN  "${CMAKE_C_FLAGS_DEBUG} -fsanitize=leak")
    set(CMAKE_C_FLAGS_MSAN  "${CMAKE_C_FLAGS_DEBUG} -fsanitize=memory -fno-optimize-sibling-calls -fno-omit-frame-pointer")
    set(CMAKE_C_FLAGS_UBSAN "${CMAKE_C_FLAGS_DEBUG} -fsanitize=undefined")
elseif(MSVC)
    set(CMAKE_C_FLAGS       "/std:c17 /W4 /WX")
    set(CMAKE_C_FLAGS_ASAN  "${CMAKE_C_FLAGS_DEBUG} /fsanitize=address")
    set(CMAKE_EXE_LINKER_FLAGS_ASAN "/debug /INCREMENTAL:NO")
endif()

if(WIN32)
    add_compile_definitions(_CRT_SECURE_NO_WARNINGS)
endif()

find_program(CLANG_TIDY clang-tidy)
message(STATUS "Clang-tidy: ${CLANG_TIDY}")
if(CLANG_TIDY)
    file(STRINGS clang-tidy-checks.txt clang_tidy_checks)
    list(JOIN clang_tidy_checks "," clang_tidy_checks_str)
    set(CMAKE_C_CLANG_TIDY
        ${CLANG_TIDY}
        -header-filter=${CMAKE_SOURCE_DIR}
        -checks=${clang_tidy_checks_str}
        -warnings-as-errors=*
    )
endif()

add_subdirectory(solution)

option(BUILD_TESTING "Enable tests" ON)
if(BUILD_TESTING)
    enable_testing()
    add_subdirectory(tester)
endif()
