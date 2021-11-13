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
