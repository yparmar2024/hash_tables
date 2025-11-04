# Space-Efficient Hash Set

This repository contains an implementation of a **space-efficient hash set** using **open addressing** and **quadratic probing**, rather than traditional chaining.

---

## 🛠️ Features

- **Basic Set Operations:** `insert`, `delete`, `contains`  
- **Collision Handling:** Quadratic probing to resolve collisions  
- **Deletion Management:** Uses tombstones (`DELETED` markers) to handle removals without breaking probe sequences  
- **Performance Tracking:** Counts accesses for performance analysis and optimization  

---

> Focused on implementing efficient data structures with attention to space and access performance.
