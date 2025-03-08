
#ifndef SPLINTER_LOG_UTIL_H
#define SPLINTER_LOG_UTIL_H

#define LOG_I(var) log_var(__FUNCTION__, #var, var)

template <typename T>
void log_var(const char* func, const char* var_name, const T& var_value) {
    std::ostringstream oss;
    oss << var_value;  // Convert any type to string
    __android_log_print(ANDROID_LOG_INFO, func, "%s: %s", var_name, oss.str().c_str());
}

#endif
