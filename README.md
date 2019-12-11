# VideoComparison
A video comparison tool based on java. This tool uses the pHash algorithm to hash a stack of frames (average frame per second). At the moment only the Windows operating system is supported. Depending on interests Linux and MacOS will follow.
This tool uses the OpenCV-411 and FFMPEG library, which are noted in `src/lib`. These files are not included in this repository.

The tools search recursively in the video directory for video. Then it generates a chain of hashes and compares the hash chains with a "Shifted Longest Common Subhash" algorithm (self defined name).
The result is then written to the defined file in CSV format. This list can then be sorted by the percentage of match.

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