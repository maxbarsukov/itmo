#include <linux/module.h>
#include <linux/version.h>
#include <linux/kernel.h>
#include <linux/types.h>
#include <linux/kdev_t.h>
#include <linux/fs.h>
#include <linux/device.h>
#include <linux/cdev.h>
#include <linux/uaccess.h>

#define BUF_SIZE 256
#define MAX_LOG_ENTRIES 50
#define LOG_BUF_SIZE (BUF_SIZE * MAX_LOG_ENTRIES)

static dev_t first;
static struct cdev c_dev;
static struct class *cl;

static char log_buf[LOG_BUF_SIZE];
static int log_size = 0;
static DEFINE_MUTEX(log_mutex);

static int my_open(struct inode *i, struct file *f)
{
  printk(KERN_INFO "Driver: open()\n");
  return 0;
}

static int my_close(struct inode *i, struct file *f)
{
  printk(KERN_INFO "Driver: close()\n");
  return 0;
}

static ssize_t my_read(struct file *f, char __user *buf, size_t len, loff_t *off)
{
  int ret = 0;

  printk(KERN_INFO "Driver: read()\n");

  mutex_lock(&log_mutex);

  if (*off >= log_size)
  {
    mutex_unlock(&log_mutex);
    return 0;
  }

  if (*off + len > log_size)
  {
    len = log_size - *off;
  }

  if (copy_to_user(buf, log_buf + *off, len) != 0)
  {
    mutex_unlock(&log_mutex);
    return -EFAULT;
  }

  *off += len;
  ret = len;

  mutex_unlock(&log_mutex);
  return ret;
}

static void log_number(int count)
{
  char int_to_str[12];
  int str_len;

  if (log_size + sizeof(int_to_str) >= LOG_BUF_SIZE)
  {
    memmove(log_buf, log_buf + log_size / 2, log_size / 2);
    log_size = log_size / 2;
  }

  str_len = snprintf(int_to_str, sizeof(int_to_str), "%d ", count);
  memcpy(log_buf + log_size, int_to_str, str_len);
  log_size += str_len;
}

static ssize_t my_write(struct file *f, const char __user *buf, size_t len, loff_t *off)
{
  char input_buf[BUF_SIZE];
  int count;

  printk(KERN_INFO "Driver: write()\n");

  if (len == 0 || len > BUF_SIZE)
  {
    return -EINVAL;
  }

  if (copy_from_user(input_buf, buf, len))
  {
    return -EFAULT;
  }

  input_buf[len] = '\0';

  mutex_lock(&log_mutex);
  count = strlen(input_buf);
  log_number(count);
  mutex_unlock(&log_mutex);

  return len;
}

static struct file_operations var1_fops = {
  .owner = THIS_MODULE,
  .open = my_open,
  .release = my_close,
  .read = my_read,
  .write = my_write
};

static int __init ch_drv_init(void)
{
  printk(KERN_INFO "Driver: Hello!\n");
  if (alloc_chrdev_region(&first, 0, 1, "ch_dev") < 0)
  {
    return -1;
  }
  if ((cl = class_create(THIS_MODULE, "chardrv")) == NULL)
  {
    unregister_chrdev_region(first, 1);
    return -1;
  }
  if (device_create(cl, NULL, first, NULL, "var1") == NULL)
  {
    class_destroy(cl);
    unregister_chrdev_region(first, 1);
    return -1;
  }
  cdev_init(&c_dev, &var1_fops);
  if (cdev_add(&c_dev, first, 1) == -1)
  {
    device_destroy(cl, first);
    class_destroy(cl);
    unregister_chrdev_region(first, 1);
    return -1;
  }

  mutex_init(&log_mutex);
  return 0;
}

static void __exit ch_drv_exit(void)
{
  cdev_del(&c_dev);
  device_destroy(cl, first);
  class_destroy(cl);
  unregister_chrdev_region(first, 1);
  printk(KERN_INFO "Bye!!!\n");
}

module_init(ch_drv_init);
module_exit(ch_drv_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("maxbarsukov");
MODULE_DESCRIPTION("Symbol reader module driver");
