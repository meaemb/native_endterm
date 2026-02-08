# Performance Note — Campus Companion

Performance was improved by using paginated loading with a page size of 20
and image downsampling via Coil AsyncImage.

Profiling was performed using Android Studio Profiler.
CPU and memory usage remain stable during feed scrolling and pagination.

No blocking operations are performed on the main thread.
