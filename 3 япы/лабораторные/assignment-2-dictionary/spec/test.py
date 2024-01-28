import unittest
import subprocess

class TestLab2(unittest.TestCase):
  def run_app(self, inp):
    pipe = subprocess.PIPE
    proc = subprocess.Popen(
      ["./bin/app"], text=True, shell=True,
      stdin=pipe, stdout=pipe, stderr=pipe
    )
    stdout, stderr = proc.communicate(input=inp)
    return (stdout.strip(), stderr.strip(), proc.returncode)

  def test_keyword(self):
    self.assertEqual(
      self.run_app("first word"),
      ("first word explanation", "", 0)
    )
    self.assertEqual(
      self.run_app("second word"),
      ("second word explanation", "", 0)
    )

  def test_not_found(self):
    out = ("", "error: Not Found", 1)
    self.assertEqual(self.run_app("a"), out)

  def test_too_long(self):
    out = ("", "error: The string is too long", 1)
    self.assertEqual(self.run_app("A"*270), out)


if __name__ == "__main__":
  unittest.main()
