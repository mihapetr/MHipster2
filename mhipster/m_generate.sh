#!/bin/bash

DESTINATION="${1}"
PACKAGE_NAME="${2}"
RETENTION_POLICY="${3}"

OUTPUT_FILE="$DESTINATION/MGenerated.java"

cat > "$OUTPUT_FILE" <<EOF
package $PACKAGE_NAME;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.$RETENTION_POLICY)
public @interface MGenerated {
}
EOF
