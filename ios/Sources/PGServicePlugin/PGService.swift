import Foundation

@objc public class PGService: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
