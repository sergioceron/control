# Validation module

VALID_STATUSES = ["active", "inactive"]

def validate_input(param_type, value):
    if param_type is None or value is None:
        raise TypeError("Parameter type and value cannot be None")

    if param_type == "status":
        if value not in VALID_STATUSES:
            raise ValueError(f"Invalid value for {param_type}: {value}")

    return True

