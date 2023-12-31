---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by huian.
--- DateTime: 2023/11/11 下午2:26
---
print("hello world!")
print(type("hello world"))
print(type(10.4*3))
print(type(true))
print(type(print))

-- 声明字符串
local str1 = 'hello'
local str2 = 'world'
print(str1 .. str2)

-- 声明数组 key为索引的table
local arr = {'java', 'python', 'lua'}
for index, value in pairs(arr) do
    print(index, value)
end
-- 声明map
local map = {name='Jack', age=21}
print(map['name'])
print(map.name)
for key, value in pairs(map) do
   print(key, value)
end

--声明方法
local function printArr(arr)
    for i, value in ipairs(arr) do
        print(i, value)
    end
end
printArr(arr)

local function printMap(map)
    if (not map) then
        print("table不能为空")
        return nil
    else
        for key, value in pairs(map) do
            print(key, value)
        end
    end
end

printMap(map)