# online-video-dl
Utility to automatically download online videos.

## Prerequisites

### python3

```shell
apt install python3
```

### pip

```shell
apt install python3-pip
```

### youtube-dl

```shell
python3 -m pip install youtube-dl
```

### ffmpeg

```shell
apt install ffmpeg
```

## Usage

### File with list of video streams

Save a file with the name `courses.yml` into `src/main/resources/` directory
which will hierarchically contain information about where to find the
video streams and how to name the resulting video files. An example
with explanation can be found in the same directory.

### Run

Run the kotlin application (function `main` in `Main.kt`).
All the videos will be downloaded into the root directory of the
project (some intermediary files will be created and deleted in
the download process by `youtube_dl` or `ffmpeg` command-line utilities).

### Common Issues

> 'python3' is not recognized as an internal or external command, operable program or batch file

Solution: Check the prerequisities, Python 3 must be installed on your system and added to path.
If you run python with `py` instead of `python3` or use some other command, edit the constant `PYTHON_COMMAND` in `Main.kt` accordingly.
If you have recently installed it and added it to path, but still get the error, restart your IDE or terminal.

> m3u8 download detected but ffmpeg or avconv could not be found. Please install one.

Solution: Check the prerequisites, `ffmpeg` command-line utility should be installed and added to path in order for `youtube_dl` to be able to use it.
If you have recently installed it and added it to path, but still get the error, restart your IDE or terminal.
