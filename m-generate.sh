#!/bin/bash

PACKAGE_NAME="${1}"
RETENTION_POLICY="${2}"
DESTINATION="${3}"
OUTPUT_FILE="$DESTINATION/MGenerated.java"

cat > "$OUTPUT_FILE" <<EOF
package $PACKAGE_NAME;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.$RETENTION_POLICY)
public @interface MGenerated {
}
EOF

