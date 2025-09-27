Jitter introduces random variation at the client level to prevent repeated, synchronized retries. When combined with exponential backoff, it helps ensure retries are spread out, giving the upstream system breathing space to recover.

Most libraries support this out of the box, allowing you to add a jitter (random) factor along with
the exponential backoff factor so that retries are not concentrated at the same time.