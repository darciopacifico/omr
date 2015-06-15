#!/bin/bash
FILES=target/dependency/*
for f in $FILES
do
  FULL=$FULL"$(pwd $f)/$f;"
done

echo "$FULL"
