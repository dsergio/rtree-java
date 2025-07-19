
#!/bin/bash

# read from environment variable 

src=${MLPA_SRC}
dest=${MLPA_DEST}

# read the CSV file and extract the first column
# using IFS to handle spaces in problem names
IFS=$'\n' read -d '' -r -a array < <(cut -d ',' -f 1 ../mlpa.csv | tail -n +2 && printf '\0')

for problem in "${array[@]}"; do
    # echo "Processing problem: $problem"
    src_path="$src/problem_$problem/report/exercise_report.pdf"
    src_path_notebooks="$src/problem_$problem/notebooks/*.html"
    dest_path="$dest/$problem"

    if [ -f "$src_path" ]; then
        echo "Copying file $src_path to $dest_path"
        echo "Copying notebooks from $src_path_notebooks to $dest_path"
        mkdir -p "$dest_path"
        cp "$src_path" "$dest_path/exercise_report.pdf"
        cp $src_path_notebooks "$dest_path/"
    else
        echo "Source path $src_path does not exist."
    fi
done