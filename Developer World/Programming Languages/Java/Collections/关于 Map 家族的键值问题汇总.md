|                                          | key 是否允许为空？                                  | key 是否允许重复？ | value 是否允许为空？ |
| ---------------------------------------- | --------------------------------------------------- | :----------------: | :------------------: |
| `java.util.HashMap`                      | 允许一个键为 `null`，但==最多只能有一个 `null` 键== |         F          |          T           |
| `java.util.LinkedHashMap`                | 同 `HashMap`                                        |         F          |          T           |
| `java.util.concurrent.ConcurrentHashMap` | F                                                   |         F          |          F           |



为何 `HashMap` 和 `LinkedHashMap` 最多允许一个 key 为 `null`？因为键是基于 `hashCode()` 和 `equals()` 方法来判断是否相等的，而 `null` 键会被==单独处理==。