if(CMAKE_C_COMPILER_ID STREQUAL GNU OR CMAKE_C_COMPILER_ID MATCHES Clang)
    add_compile_options(-Wall -Werror -Wno-unused -ggdb)
elseif(MSVC)
    add_compile_options(/W4 /WX)
endif()

find_program(CLANG_TIDY clang-tidy)
message(STATUS "Clang-tidy: ${CLANG_TIDY}")
if(CLANG_TIDY)
    file(STRINGS ${PROJECT_SOURCE_DIR}/clang-tidy-checks.txt clang_tidy_checks)
    list(JOIN clang_tidy_checks "," clang_tidy_checks_str)
    set(CMAKE_C_CLANG_TIDY
        ${CLANG_TIDY}
        -header-filter=${CMAKE_SOURCE_DIR}
        -checks=${clang_tidy_checks_str}
        -warnings-as-errors=*
    )
endif()

file(GLOB_RECURSE sources CONFIGURE_DEPENDS *.c *.h)
list(FILTER sources EXCLUDE REGEX main.c)

add_library(memalloc STATIC ${sources})
target_include_directories(memalloc PUBLIC .)

if(EXISTS ${CMAKE_CURRENT_SOURCE_DIR}/main.c)
    add_executable(memalloc_exe main.c)
    target_link_libraries(memalloc_exe PRIVATE memalloc)
    set_target_properties(memalloc_exe PROPERTIES OUTPUT_NAME memalloc)
endif()
