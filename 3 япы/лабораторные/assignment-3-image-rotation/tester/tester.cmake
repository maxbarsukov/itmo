# CMP0007: list command no longer ignores empty elements.
if(POLICY CMP0007)
    cmake_policy(SET CMP0007 NEW)
endif()

function(exec_check)
    execute_process(COMMAND ${ARGV}
        OUTPUT_VARIABLE out
        ERROR_VARIABLE  err
        RESULT_VARIABLE result)
    if(result)
        string(REPLACE "/" ";" name_components ${ARGV0})
        list(GET name_components -1 name)
        if(NOT out)
            set(out "<empty>")
        endif()
        if(NOT err)
            set(err "<empty>")
        endif()
        message(FATAL_ERROR "\nError running \"${name}\"\n*** Output: ***\n${out}\n*** Error: ***\n${err}\n")
    endif()
endfunction()

file(STRINGS ${TEST_DIR}/angle ANGLE)
file(REMOVE ${TEST_DIR}/output.bmp)
exec_check(${IMAGE_TRANSFORMER} ${TEST_DIR}/input.bmp ${TEST_DIR}/output.bmp ${ANGLE})
exec_check(${IMAGE_MATCHER} ${TEST_DIR}/output.bmp ${TEST_DIR}/output_expected.bmp)
