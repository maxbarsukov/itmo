%define last_label 0

%macro colon 2
  %ifid %2
      %2:
          dq last_label
          db %1, 0

      %define last_label %2
  %else
      %error "error: Invalid identifier"
  %endif
%endmacro
