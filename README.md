Http4s 1.0.0-M32 - test symbolic link
=====================

#### Create a text file and a symbolic link:

```
yes "Some text" | head -n 1000 > /tmp/myfile.txt
ln -s /tmp/myfile.txt /tmp/link.txt
```

#### Test

`sbt test`