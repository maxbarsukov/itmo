file(GLOB_RECURSE sources CONFIGURE_DEPENDS
    src/*.c
    src/*.h
    include/*.h
)

add_executable(image-matcher ${sources})
target_include_directories(image-matcher PRIVATE src include)

file(GLOB test_directories CONFIGURE_DEPENDS tests/*)
list(FILTER test_directories EXCLUDE REGEX ".*/\.gitignore")

foreach(test_dir IN LISTS test_directories)
    string(REPLACE "/" ";" name_components ${test_dir})
    list(GET name_components -1 name)
    add_test(NAME test-${name}
        COMMAND ${CMAKE_COMMAND}
            -DTEST_DIR=${test_dir}
            -DIMAGE_TRANSFORMER=$<TARGET_FILE:image-transformer>
            -DIMAGE_MATCHER=$<TARGET_FILE:image-matcher>
            -P ${CMAKE_CURRENT_SOURCE_DIR}/tester.cmake
    )
endforeach()

set(CMAKE_CTEST_ARGUMENTS --output-on-failure -C $<CONFIG>)
add_custom_target(check
    COMMAND ${CMAKE_CTEST_COMMAND} ${CMAKE_CTEST_ARGUMENTS}
    DEPENDS image-transformer image-matcher)
