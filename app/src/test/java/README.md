# VersionCode 5, VersionName 1.4

- Remove languages column in CSV (because it wasn't being used and was fragile)
- Simplify the grouping logic so all groups must have a selection before any observations are recorded (and record the whole state when the first observation is record)
