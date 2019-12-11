# VideoComparison
A video comparison tool based on java. This tool uses the pHash algorithm to hash a stack of frames (average frame per second). At the moment only the Windows operating system is supported. Depending on interests Linux and MacOS will follow.
This tool uses the OpenCV-411 and FFMPEG library, which are noted in `src/lib`. These files are not included in this repository.
## How It Works
The tools search recursively in the video directory for video. Then it generates a chain of hashes and compares the hash chains with a "Shifted Longest Common Subhash" algorithm (self defined name).
The result is then written to the defined file in CSV format. This list can then be sorted by the percentage of match.<br>

Let's assume the following folder structure is given and the video path is set to `/users/admin/video`.
```
/
├── users/
│   ├── admin/
│   │   ├── video
│   │   │   ├── dcim
│   │   │   │   ├── a.mp4
│   │   │   │   └── b.mp4
│   │   │   ├── backups
│   │   │   │   ├── aa.mp4
│   │   │   │   └── b.mp4
│   │   │   └── vacation2019
│   │   │       └── c.mp4
```
In addition, the following is assumed:<br>
- a != b != c
- a ~ aa
- b == b
In this case the file with the results could look like this (but without spaces):<br>
```
Video-A;                     Video-B;                           Consistency
/users/admin/dcim/a.mp4;     /users/admin/dcim/b.mp4;           0.1
/users/admin/dcim/a.mp4;     /users/admin/backups/aa.mp4;       89
/users/admin/dcim/a.mp4;     /users/admin/backups/b.mp4;        0
/users/admin/dcim/a.mp4;     /users/admin/vacation2019/c.mp4;   9.2
/users/admin/dcim/b.mp4;     /users/admin/backups/aa.mp4;       1.4
/users/admin/dcim/b.mp4;     /users/admin/backups/b.mp4;        99.943
/users/admin/dcim/b.mp4;     /users/admin/vacation2019/c.mp4;   22.8
/users/admin/backups/aa.mp4; /users/admin/backups/b.mp4;        1.4
/users/admin/backups/aa.mp4; /users/admin/vacation2019/c.mp4;   8.6
/users/admin/backups/b.mp4;  /users/admin/vacation2019/c.mp4;   22.8
```
## Parameters
At the moment the following parameters are offered:
```
{-vd,-video_directory}          Required: yes   Video directory
{-rp,-result_path}              Required: no    Result path (default './result.csv')
{-pc,-phash_collection}         Required: no    Select a pHash collection source and/or saving destination (default '')
{-t,-threads}                   Required: no    Number of threads (default '1')
{-hs,-hash_size}                Required: no    Size of the hash (default '256')
{-senp,-save_each_n_percentage} Required: no    How often (percentage steps) should the hashs be saved (0=only at end) (default '10')
{-duh,-drop_unneeded_hashs}     Required: no    Drop unneeded hashs in given collection (default 'no')
```
